package br.com.sicot.data;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import br.com.sicot.model.DescontoObtido;
import br.com.sicot.model.RelatorioUltimaCompar;
import br.com.sicot.util.Resources;

/**
 * Created by thiago on 05/02/16.
 */
@Stateless
public class RelatorioClienteListProducer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
    private Resources resource;

    @PostConstruct
    public void init() {
    	
    }
    
    @SuppressWarnings("unchecked")
	public List<RelatorioUltimaCompar> valorePagosDozeMes(String id){
    	
    	Query query = resource.createQuery(RelatorioUltimaCompar.DIFF_TWELVE_MONTH, RelatorioUltimaCompar.class);
    	query.setParameter(1, id);
    	
    	return query.getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<DescontoObtido> valoreDescontoSeisMes(String id){
    	
    	Query query = resource.createQuery(DescontoObtido.SUM_SIX_DESCONTO, DescontoObtido.class);
    	query.setParameter(1, id);
    	
    	return query.getResultList();
    }
    
	public DescontoObtido valoreTotalDesconto(String id){
    	
    	Query query = resource.createQuery(DescontoObtido.SUM_DESCONTO, DescontoObtido.class);
    	query.setParameter(1, id);
    	
    	return (DescontoObtido) query.getSingleResult();
    }
    
   	public DescontoObtido valoreTotalCompra(String id){
       	
       	Query query = resource.createQuery(DescontoObtido.SUM_COMPRA, DescontoObtido.class);
       	query.setParameter(1, id);
       	
       	return (DescontoObtido) query.getSingleResult();
    }
	
}
