package br.com.sicot.service;

import br.com.sicot.model.Categoria;
import br.com.sicot.model.MarcaFabricante;
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
public class MarcaFabricanteRegistration implements Serializable{
    @Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private Event<MarcaFabricante> marcaFabricanteEventSrc;

    public void register(MarcaFabricante marcaFabricante) throws Exception {
        log.info("Registrando " + marcaFabricante.getNome());

        if (marcaFabricante.getId()==null){
            resource.insert(marcaFabricante);
        }else {
            resource.update(marcaFabricante);
        }
        marcaFabricanteEventSrc.fire(marcaFabricante);
    }

    public MarcaFabricante findById(Long id) throws Exception {
        return (MarcaFabricante) resource.findById(MarcaFabricante.class, id);
    }

    public void remove(MarcaFabricante marcaFabricante) throws Exception {
        resource.remove(MarcaFabricante.class, marcaFabricante.getId());
    }
}
