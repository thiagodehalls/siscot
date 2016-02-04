package br.com.sicot.controller;

import br.com.sicot.model.Categoria;
import br.com.sicot.service.CategoriaRegistration;
import br.com.sicot.util.Constantes;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Model
public class CategoriaController {

   @Inject
   private FacesContext facesContext;

   @Inject
   private CategoriaRegistration categoriaRegistration;

   private Categoria newCategoria;

   private Long id;

   public Long getId() {
        return this.id;
   }

   public void setId(Long id) {
        this.id = id;
   }

   @Produces
   @Named
   public Categoria getNewCategoria() {
      return newCategoria;
   }

   public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.id == null) {
            initNewCategoria();
        } else {
            try {
                this.newCategoria = categoriaRegistration.findById(getId());
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
   }


   public void register() throws Exception {
       try {
           categoriaRegistration.register(newCategoria);
           FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
           facesContext.addMessage(null, m);
           initNewCategoria();
       } catch (Exception e) {
           String errorMessage = getRootErrorMessage(e);
           FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
           facesContext.addMessage(null, m);
       }
   }

    public void remove() throws  Exception {
        try{
            categoriaRegistration.remove(newCategoria);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewCategoria();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao excluir.");
            facesContext.addMessage(null, m);
        }
    }

   @PostConstruct
   public void initNewCategoria() {
      newCategoria = new Categoria();
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
