package br.com.sicot.service;

import br.com.sicot.model.Categoria;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CategoriaRegistration implements Serializable{

   @Inject
   private Logger log;

   @Inject
   private Resources resource;

   @Inject
   private Event<Categoria> categoriaEventSrc;

   public void register(Categoria categoria) throws Exception {
      log.info("Registrando " + categoria.getNome());
      if (categoria.getId()==null){
          resource.insert(categoria);
      }else{
          resource.update(categoria);
      }

      categoriaEventSrc.fire(categoria);
   }

   public Categoria findById(Long id) throws Exception {
       log.info("Find by id " + id);
       return (Categoria) resource.findById(Categoria.class, id);
   }

    public void remove(Categoria categoria) throws Exception {
        resource.remove(Categoria.class, categoria.getId());
    }
}
