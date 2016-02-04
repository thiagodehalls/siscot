package br.com.sicot.service;

import br.com.sicot.model.Categoria;
import br.com.sicot.model.ListaCompra;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ListaCompraRegistration implements Serializable{

   @Inject
   private Logger log;

   @Inject
   private Resources resource;

   @Inject
   private Event<ListaCompra> listaCompraEventSrc;

   public void register(ListaCompra listaCompra) throws Exception {
      log.info("Registrando " + listaCompra.getNome());
      if (listaCompra.getId()==null){
          resource.insert(listaCompra);
      }else{
          resource.update(listaCompra);
      }

      listaCompraEventSrc.fire(listaCompra);
   }

   public ListaCompra findById(Long id) throws Exception {
       log.info("Find by id " + id);
       ListaCompra listaCompra = (ListaCompra) resource.findById(ListaCompra.class, id);
       inicialize(listaCompra);
       return listaCompra;

   }

    private void inicialize(ListaCompra listaCompra) {
        listaCompra.getItemsListaCompra().size();
    }

    public void remove(ListaCompra listaCompra) throws Exception {
        resource.remove(ListaCompra.class, listaCompra.getId());
    }
}
