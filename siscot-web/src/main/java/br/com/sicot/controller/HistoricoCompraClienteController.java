package br.com.sicot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import br.com.sicot.data.HistoricoCompraClienteProducer;
import br.com.sicot.data.RelatorioClienteListProducer;
import br.com.sicot.model.Compra;
import br.com.sicot.util.ClienteCompra;

@Named
@Stateful
@ConversationScoped
public class HistoricoCompraClienteController {

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private Conversation conversation;
	
	@Inject
	private HistoricoCompraClienteProducer producer;
	
	@Inject
	private ClienteCompra clienteCompra;

	private List<Compra> compras;

	private Compra filtroCompra;
	
	private String periodo;
	
	private String situacao;

	@PostConstruct
	private void init() {
		filtroCompra = new Compra();
		situacao = "";
	}

	@Named
	@Produces
	public Compra getFiltroCompra() {
		return filtroCompra;
	}

	public void filtrar() throws ParseException {

		if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }
		
		filtroCompra = new Compra();
		filtroCompra.setCliente(clienteCompra.getClienteCompra());
		
		if(periodo!=null && !periodo.isEmpty()){
			
			String datas[] = periodo.split(" - ");
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
			
			filtroCompra.setDataInicio(sdf.parse(datas[0]));
			filtroCompra.setDataFim(sdf.parse(datas[1]));
		}
		
		filtroCompra.setSituacao(situacao);
		
		compras = producer.findHistoricoCompra(filtroCompra);

		if(compras==null || compras.isEmpty()){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nenhuma compra atende os parametros informados.", 
																				"Nenhuma compra atende os parametros informados");
	        facesContext.addMessage(null, m);
		}
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
}
