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
import br.com.sicot.model.Compra;

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

	private List<Compra> compras;

	private Compra filtroCompra;
	
	private String periodo;

	@PostConstruct
	private void init() {
		filtroCompra = new Compra();
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
		
		if(periodo!=null && !periodo.isEmpty()){
			
			String datas[] = periodo.split(" - ");
			
			SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
			
			filtroCompra.setDataInicio(sdf.parse(datas[0]));
			filtroCompra.setDataInicio(sdf.parse(datas[1]));
		}
		
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

}
