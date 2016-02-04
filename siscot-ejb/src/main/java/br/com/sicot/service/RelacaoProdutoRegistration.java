package br.com.sicot.service;

import br.com.sicot.model.Produto;
import br.com.sicot.model.RelacaoProduto;
import br.com.sicot.model.RelacaoProdutoPK;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by thiago on 11/12/15.
 */
@Stateless
public class RelacaoProdutoRegistration implements Serializable {
    @Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private Event<RelacaoProduto> relacaoProdutoEventSrc;

    public void register(RelacaoProduto relacaoProduto) throws Exception{
        log.info("Registrando relação de produto");

        resource.insert(relacaoProduto);

        relacaoProdutoEventSrc.fire(relacaoProduto);
    }

    public RelacaoProduto findById(RelacaoProdutoPK id) {
        RelacaoProduto relacaoProduto = (RelacaoProduto) resource.findById(RelacaoProduto.class, id);
        this.inicialize(relacaoProduto);
        return relacaoProduto;
    }

    private void inicialize(RelacaoProduto relacaoProduto) {
        relacaoProduto.getProduto().getDescricao();
        relacaoProduto.getProdutoRelacao().getDescricao();
    }

    public void remove(RelacaoProduto relacaoProduto){
        log.info("Excluindo relação de produto");

        RelacaoProdutoPK id = new RelacaoProdutoPK();
        id.setProdutoId(relacaoProduto.getProdutoId());
        id.setProdutoIdRelacao(relacaoProduto.getProdutoIdRelacao());

        resource.remove(RelacaoProduto.class, id);
    }

}
