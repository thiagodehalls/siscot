package br.com.sicot.controller;

import br.com.sicot.model.Cliente;
import br.com.sicot.model.User;
import br.com.sicot.service.ClienteRegistration;
import br.com.sicot.service.UserRegistration;
import br.com.sicot.util.Constantes;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by thiago on 15/12/15.
 */
@Model
public class ClienteController {
    private static final String PAGINA_INDEX = "/index2.xhtml?faces-redirect=true";

    @Inject
    private FacesContext facesContext;

    @Inject
    private ClienteRegistration clienteRegistration;

    private Cliente newCliente;

    @Produces
    @Named
    public Cliente getNewCliente() {
        return newCliente;
    }

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

        if (this.id == null) {
            initNewCliente();
        } else {
            try {
                this.newCliente = clienteRegistration.findByEmail(getId());
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    public String register() throws Exception {
        try {
            newCliente.setSituacao("P");//P - Pendente
            newCliente.setCidade(new Short("1").shortValue());
            clienteRegistration.register(newCliente);

            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);

            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            request.getSession().setAttribute("USUARIO", generateUser());

            initNewCliente();

            return PAGINA_INDEX;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }

        return null;
    }

    private User generateUser() {
        User user = new User();
        user.setId(newCliente.getEmail());
        user.setPassword(newCliente.getPassword());
        user.setNome(newCliente.getNome());

        return user;
    }

    @PostConstruct
    public void initNewCliente() {
        newCliente = new Cliente();
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
