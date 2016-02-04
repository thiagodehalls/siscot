package br.com.sicot.service;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import br.com.sicot.data.EstoqueListProducer;
import br.com.sicot.model.Compra;
import br.com.sicot.model.Estoque;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.util.Resources;

/**
 * Created by thiago on 04/12/15.
 */
@Stateless
public class CompraRegistration implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private EstoqueListProducer estoqueListProducer;

    @Inject
    private Event<Compra> compraEventSrc;
    
    @Inject
    private Event<ItemCompra> itemCompraEventSrc;

    public void register(Compra compra) throws Exception {
        log.info("Registrando " + compra.getId());

        if(compra.getId()==null) {
            compra.setDataHora(new Date());
            resource.insert(compra);

            for (ItemCompra itemCompra : compra.getItemCompras()) {
                itemCompra.setCompraId(compra.getId());
                itemCompra.getValorObtidoDesconto();
                itemCompra.setSituacao("P");
                resource.insert(itemCompra);
            }

        }else{
            resource.update(compra);
        }

        compraEventSrc.fire(compra);
    }
    
    public void updateItemComprar(ItemCompra itemCompra) throws Exception {
    	log.info("Atualizado item compra produto " + itemCompra.getProduto().getId() + " compra " + itemCompra.getCompraId());
    	resource.update(itemCompra);
    	itemCompraEventSrc.fire(itemCompra);
    }

    private void inicialize(Compra compra) {
        compra.getItemCompras().size();
    }

    public void obterValores(Compra compra){

        for(ItemCompra item : compra.getItemCompras()){

            Estoque estoque = estoqueListProducer
                    .findEstoqueProdutoQuantidadeOrderByValorUnitario(item.getProduto());

            item.setValorUnitario(estoque.getValorUnitario());
            item.setMercadoCnpj(estoque.getCnpj());
            item.setEmpresa(estoque.getEmpresa());
        }
    }

    public Compra findById(Long id) {
        Compra compra = (Compra) resource.findById(Compra.class, id);

        inicialize(compra);
        return compra;
    }
}
