package br.com.sicot.service;

import br.com.sicot.model.Produto;
import br.com.sicot.model.UnidadeMedida;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by thiago on 04/12/15.
 */
@Stateless
public class ProdutoRegistration implements Serializable{

    @Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private Event<Produto> produtoEventSrc;

    public void register(Produto produto) throws Exception {
        log.info("Registrando " + produto.getNome());

        if(produto.getId()==null) {
            resource.insert(produto);
        }else{
            resource.update(produto);
        }

        produtoEventSrc.fire(produto);
    }

    public Produto findById(Long id) {
        Produto produto = (Produto) resource.findById(Produto.class, id);
        this.inicialize(produto);
        return produto;
    }

    private void inicialize(Produto produto) {
        produto.getImagems().size();
        produto.getAtributos().size();
    }

    public void remove(Produto produto){
        resource.remove(Produto.class, produto.getId());
    }

}
