package br.com.sicot.service;

import br.com.sicot.model.MarcaFabricante;
import br.com.sicot.model.UnidadeMedida;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by thiago on 01/12/15.
 */
@Stateless
public class UnidadeMedidaRegistration implements Serializable{
    @Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private Event<UnidadeMedida> unidadeMedidaEventSrc;

    public void register(UnidadeMedida unidadeMedida) throws Exception {
        log.info("Registrando " + unidadeMedida.getNome());

        if(unidadeMedida.getId()==null){
            resource.insert(unidadeMedida);
        }else{
            resource.update(unidadeMedida);
        }

        unidadeMedidaEventSrc.fire(unidadeMedida);
    }

    public UnidadeMedida findById(Long id) throws Exception {
        return (UnidadeMedida) resource.findById(UnidadeMedida.class, id);
    }

    public void remove(UnidadeMedida unidadeMedida) throws Exception {
        resource.remove(UnidadeMedida.class, unidadeMedida.getId());
    }
}
