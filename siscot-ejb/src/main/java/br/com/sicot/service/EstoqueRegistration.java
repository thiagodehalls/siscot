package br.com.sicot.service;

import br.com.sicot.model.Empresa;
import br.com.sicot.model.Estoque;
import br.com.sicot.model.EstoquePK;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by thiago on 14/12/15.
 */
@Stateless
public class EstoqueRegistration implements Serializable {

    @Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private Event<Estoque> estoqueEventSrc;

    public void register(Estoque estoque) throws Exception {
        log.info("Registrando estoque");

        EstoquePK id = new EstoquePK();
        id.setCnpj(estoque.getCnpj());
        id.setProdutoId(estoque.getProdutoId());

        if(findById(id)==null) {
            resource.insert(estoque);
        }else{
            resource.update(estoque);
        }

        estoqueEventSrc.fire(estoque);
    }

    public Estoque findById(EstoquePK id) {
        Estoque estoque = (Estoque) resource.findById(Estoque.class, id);
        return estoque;
    }
}
