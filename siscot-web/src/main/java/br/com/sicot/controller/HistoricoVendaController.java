package br.com.sicot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sicot.data.HistoricoVendaEmpresaProducer;
import br.com.sicot.data.ItemCompraListProducer;
import br.com.sicot.model.Compra;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.util.EmpresaEstoque;
import br.com.sicot.vo.SubCompra;

@Named
@Stateful
@ConversationScoped
public class HistoricoVendaController {

	@Inject
	private FacesContext facesContext;

	@Inject
	private Conversation conversation;

	@Inject
	private HistoricoVendaEmpresaProducer producer;

	@Inject
	private EmpresaEstoque empresaEstoque;
	
	@Inject
	private ItemCompraListProducer itemCompraListProducer;

	private List<Compra> compras;

	private Compra filtroVenda;

	private String periodo;

	private String situacao;
	
    private List<ItemCompra> itensCompras;

    private SubCompra subCompra;
    
    private String id;

    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }

	@PostConstruct
	private void init() {
		filtroVenda = new Compra();
		situacao = "";
	}

	@Produces
    @Named
    public List<ItemCompra> getItensCompras() {
    	return this.itensCompras;
    }

	@Named
	@Produces
	public Compra getFiltroVenda() {
		return filtroVenda;
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
				load();
			} catch (Exception e) {
				String errorMessage = getRootErrorMessage(e);
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
						"Falha na consulta por id.");
				facesContext.addMessage(null, m);
			}
		}
	}
	
	 private void load() {
    	if(empresaEstoque.getEmpresaEstoque()!=null) {
    		itensCompras = itemCompraListProducer.findCompraPendenteOrderByData(empresaEstoque.getEmpresaEstoque(), Integer.parseInt(this.id.trim()));
    		
    		if(itensCompras!=null){
    			generateSubCompra();
    		}
    	}
    }
    
    private void generateSubCompra() {
        subCompra = new SubCompra();
        subCompra.setItens(new ArrayList<ItemCompra>());
        
        for (ItemCompra item : this.itensCompras) {
        	subCompra.setCliente(item.getCompra().getCliente());
        	subCompra.getItens().add(item);
        }
    }

	public void filtrar() throws ParseException {

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		filtroVenda = new Compra();
		filtroVenda.setCnpj(empresaEstoque.getEmpresaEstoque().getCnpj());

		if (periodo != null && !periodo.isEmpty()) {

			String datas[] = periodo.split(" - ");

			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");

			filtroVenda.setDataInicio(sdf.parse(datas[0]));
			filtroVenda.setDataFim(sdf.parse(datas[1]));
		}

		filtroVenda.setSituacao(situacao);

		compras = producer.findHistoricoCompra(filtroVenda);

		if (compras == null || compras.isEmpty()) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Nenhuma compra atende os parametros informados.",
					"Nenhuma compra atende os parametros informados");
			facesContext.addMessage(null, m);
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

	public List<Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public SubCompra getSubCompra() {
		return subCompra;
	}

	public void setSubCompra(SubCompra subCompra) {
		this.subCompra = subCompra;
	}
	
}
