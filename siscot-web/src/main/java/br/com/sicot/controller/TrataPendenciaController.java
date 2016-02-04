package br.com.sicot.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sicot.data.ItemCompraListProducer;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.model.Produto;
import br.com.sicot.service.CompraRegistration;
import br.com.sicot.util.EmpresaEstoque;
import br.com.sicot.vo.ProdutoPendente;
import br.com.sicot.vo.SubCompra;

@Named
@Stateful
@ConversationScoped
public class TrataPendenciaController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
    private FacesContext facesContext;

    @Inject
    private Conversation conversation;
    
    @Inject
    private EmpresaEstoque empresaEstoque;
	
    @Inject
    private ItemCompraListProducer itemCompraListProducer;
    
    @Inject
    private CompraRegistration compraRegistration;

    private List<ItemCompra> itensComprasPendentes;
    
    private String id;

	private SubCompra subCompra;

    @Produces
    @Named
    public List<ItemCompra> getItensComprasPendentes() {
    	return this.itensComprasPendentes;
    }

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

        if (this.id != null) {
            try {
                init();
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }
    
    public String aprovada(ItemCompra itemCompra){ 
    	 try {
	    	itemCompra.setSituacao("A");
	    	compraRegistration.updateItemComprar(itemCompra);
    	 } catch (Exception e) {
             String errorMessage = getRootErrorMessage(e);
             FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na aprovação do item.");
             facesContext.addMessage(null, m);
         }
    	 
    	 init();
    	 
    	 tratarPendenciaCompra(itemCompra);
    	 
    	 if(!itemCompra.getCompra().getSituacao().equals("P")){
    		 return "/public/empresa/visualiza.xhtml?faces-redirect=true";
    	 }
    	 
    	 return null;
    }
    
	public String aprovada(Produto produto) {
		itensComprasPendentes = itemCompraListProducer.findCompraPendenteOrderByData(empresaEstoque.getEmpresaEstoque());
		
		for (ItemCompra itemCompra : itensComprasPendentes) {
			if(itemCompra.getProduto().equals(produto)){
				
				try {
					itemCompra.setSituacao("A");
					compraRegistration.updateItemComprar(itemCompra);
				} catch (Exception e) {
					String errorMessage = getRootErrorMessage(e);
					FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na aprovação do item.");
					facesContext.addMessage(null, m);
				}
				
				tratarPendenciaCompra(itemCompra);
			}
		}
		
		 return "/public/empresa/pendentes.xhtml?faces-redirect=true";

	}
    
	public String recusar(ItemCompra itemCompra) {
		try {
			itemCompra.setSituacao("R");
			compraRegistration.updateItemComprar(itemCompra);
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na aprovação do item.");
			facesContext.addMessage(null, m);
		}
		
		init();
		
		tratarPendenciaCompra(itemCompra);
		
		if(!itemCompra.getCompra().getSituacao().equals("P")){
	   		 return "/public/empresa/visualiza.xhtml?faces-redirect=true";
	   	}
   	 
   	 	return null;
	}

	private void tratarPendenciaCompra(ItemCompra itemCompra){
		
		try{
			if(itensComprasPendentes==null || itensComprasPendentes.isEmpty()){
				itemCompra.getCompra().setSituacao("A");
				compraRegistration.register(itemCompra.getCompra());
			}
		}catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na aprovação do item.");
			facesContext.addMessage(null, m);
		}
	}
	
    private void init() {
    	if(empresaEstoque.getEmpresaEstoque()!=null) {
    		itensComprasPendentes = itemCompraListProducer.findCompraPendenteOrderByData(empresaEstoque.getEmpresaEstoque(), Integer.parseInt(this.id.trim()));
    		
    		if(itensComprasPendentes!=null){
    			generateSubCompra();
    		}
    	}
    }
    
    private void generateSubCompra() {
        subCompra = new SubCompra();
        subCompra.setItens(new ArrayList<ItemCompra>());
        
        for (ItemCompra item : this.itensComprasPendentes) {
        	subCompra.setCliente(item.getCompra().getCliente());
        	subCompra.getItens().add(item);
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
    
    public SubCompra getSubCompra() {
        return subCompra;
    }

    public void setSubCompra(SubCompra subCompra) {
        this.subCompra = subCompra;
    }
}