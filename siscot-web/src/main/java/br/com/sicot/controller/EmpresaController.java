package br.com.sicot.controller;

import br.com.sicot.model.Empresa;
import br.com.sicot.model.User;
import br.com.sicot.service.EmpresaRegistration;
import br.com.sicot.service.UserRegistration;
import br.com.sicot.util.Constantes;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by thiago on 14/12/15.
 */
@Named
@Stateful
@ConversationScoped
public class EmpresaController implements Serializable {
    private static final String PASSWORD_DEFAULT = "12345678" ;
    @Inject
    private FacesContext facesContext;

    @Inject
    private EmpresaRegistration empresaRegistration;

    @Inject
    private UserRegistration userRegistration;

    @Inject
    private Conversation conversation;

    private Empresa newEmpresa;

    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }

        if (this.id == null) {
            initNewEmpresa();
        } else {
            try {
                this.newEmpresa = empresaRegistration.findById(getId());
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    @Produces
    @Named
    public Empresa getNewEmpresa() {
        return newEmpresa;
    }

    public void register() throws Exception {
        this.conversation.end();
        try{
            if(newEmpresa.getSituacao()==null || newEmpresa.getSituacao().isEmpty()){
                newEmpresa.setSituacao("P");
            }
            if(newEmpresa.getPassword()==null || newEmpresa.getPassword().isEmpty()){
                newEmpresa.setPassword(PASSWORD_DEFAULT);
            }
            if(newEmpresa.getCidade()==null){
                newEmpresa.setCidade(new Long(1));//Formosa
            }
            empresaRegistration.register(newEmpresa);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewEmpresa();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
    }

    public String registerExterno() throws Exception {
        this.conversation.end();
        try{
            if(newEmpresa.getSituacao()==null || newEmpresa.getSituacao().isEmpty()){
                newEmpresa.setSituacao("P");
            }
            if(newEmpresa.getPassword()==null || newEmpresa.getPassword().isEmpty()){
                newEmpresa.setPassword(PASSWORD_DEFAULT);
            }
            if(newEmpresa.getCidade()==null){
                newEmpresa.setCidade(new Long(1));//Formosa
            }
            empresaRegistration.register(newEmpresa);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

            final User user = userRegistration.findById(newEmpresa.getEmail());
            request.getSession().setAttribute("USUARIO", user);

            final Empresa empresa = empresaRegistration.findById(newEmpresa.getCnpj());
            request.getSession().setAttribute("EMPRESA", empresa);

            initNewEmpresa();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
            return null;
        }
        return LoginController.PAGINA_INDEX_3;
    }

    public void remove() throws  Exception {
        try{
            empresaRegistration.remove(newEmpresa);
            this.conversation.end();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewEmpresa();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao excluir.");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    private void initNewEmpresa() {
        newEmpresa = new Empresa();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
}
