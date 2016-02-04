package br.com.sicot.rest;

import br.com.sicot.model.Categoria;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/categorias")
@RequestScoped
public class CategoriaResourceRESTService {
   @Inject
   private EntityManager em;

   @GET
   @Produces("text/xml")
   public List<Categoria> listAllCategorias() {
      final List<Categoria> results = em.createQuery("select c from Categoria c order by c.nome").getResultList();
      return results;
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("text/xml")
   public Categoria lookupCategoriaById(@PathParam("id") int id) {
      return em.find(Categoria.class, id);
   }
}
