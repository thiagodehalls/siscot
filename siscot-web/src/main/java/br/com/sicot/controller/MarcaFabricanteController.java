package br.com.sicot.controller;

import br.com.sicot.model.Categoria;
import br.com.sicot.model.MarcaFabricante;
import br.com.sicot.service.CategoriaRegistration;
import br.com.sicot.service.MarcaFabricanteRegistration;
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
public class MarcaFabricanteController {
    @Inject
    private FacesContext facesContext;

    @Inject
    private MarcaFabricanteRegistration marcaFabricanteRegistration;

    private MarcaFabricante newMarcaFabricante;

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
            initNewMarcaFabricante();
        } else {
            try {
                this.newMarcaFabricante = marcaFabricanteRegistration.findById(getId());
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    @Produces
    @Named
    public MarcaFabricante getNewMarcaFabricante() {
        return newMarcaFabricante;
    }

    public void register() throws  Exception {
        try{
            marcaFabricanteRegistration.register(newMarcaFabricante);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewMarcaFabricante();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
    }

    public void remove() throws  Exception {
        try{
            marcaFabricanteRegistration.remove(newMarcaFabricante);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewMarcaFabricante();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao excluir.");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    private void initNewMarcaFabricante() {
        newMarcaFabricante = new MarcaFabricante();
    }

    public void loadNewMarcaFabricante(final MarcaFabricante marcaFabricante){
        this.newMarcaFabricante = marcaFabricante;
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
