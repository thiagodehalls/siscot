package br.com.sicot.controller;

import br.com.sicot.model.MarcaFabricante;
import br.com.sicot.model.UnidadeMedida;
import br.com.sicot.service.MarcaFabricanteRegistration;
import br.com.sicot.service.UnidadeMedidaRegistration;
import br.com.sicot.util.Constantes;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by thiago on 01/12/15.
 */
@Model
public class UnidadeMedidaController {
    @Inject
    private FacesContext facesContext;

    @Inject
    private UnidadeMedidaRegistration unidadeMedidaRegistration;

    private UnidadeMedida newUnidadeMedida;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.id == null) {
            initNewUnidadeMedida();
        } else {
            try {
                this.newUnidadeMedida = unidadeMedidaRegistration.findById(getId());
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    @Produces
    @Named
    public UnidadeMedida getNewUnidadeMedida() {
        return newUnidadeMedida;
    }

    public void register() throws  Exception {
        try{
            unidadeMedidaRegistration.register(newUnidadeMedida);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewUnidadeMedida();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
    }

    public void remove() throws  Exception {
        try{
            unidadeMedidaRegistration.remove(newUnidadeMedida);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewUnidadeMedida();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao excluir.");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    private void initNewUnidadeMedida() {
        newUnidadeMedida = new UnidadeMedida();
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
