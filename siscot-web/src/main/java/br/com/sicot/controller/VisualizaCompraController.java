package br.com.sicot.controller;

import br.com.sicot.model.Compra;
import br.com.sicot.model.Empresa;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.service.CompraRegistration;
import br.com.sicot.vo.SubCompra;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thiago on 29/12/15.
 */
@Named
@Stateful
@ConversationScoped
public class VisualizaCompraController implements Serializable{

    @Inject
    private FacesContext facesContext;

    @Inject
    private Conversation conversation;

    @Inject
    private CompraRegistration compraRegistration;

    private List<SubCompra> subCompras;

    private Compra viewCompra;

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

        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }

        if (this.id == null) {
            return;
        } else {
            try {
                this.viewCompra = compraRegistration.findById(getId());

                generateSubCompra();

            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    private void generateSubCompra() {
        subCompras = new ArrayList<SubCompra>();
        HashMap<Empresa, List<ItemCompra>> map = new HashMap<Empresa, List<ItemCompra>>();

        for (ItemCompra item : this.viewCompra.getItemCompras()) {
            if(map.get(item.getEmpresa())==null){
                map.put(item.getEmpresa(), new ArrayList<ItemCompra>());
            }

            map.get(item.getEmpresa()).add(item);
        }

        for (Empresa empresa : map.keySet()) {
            SubCompra subCompra = new SubCompra();
            subCompra.setEmpresa(empresa);

            for(ItemCompra item : map.get(empresa)){
                subCompra.getItens().add(item);
            }

            subCompras.add(subCompra);
        }
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

    public List<SubCompra> getSubCompras() {
        return subCompras;
    }

    public void setSubCompras(List<SubCompra> subCompras) {
        this.subCompras = subCompras;
    }

	public Compra getViewCompra() {
		return viewCompra;
	}

	public void setViewCompra(Compra viewCompra) {
		this.viewCompra = viewCompra;
	}
    
}
