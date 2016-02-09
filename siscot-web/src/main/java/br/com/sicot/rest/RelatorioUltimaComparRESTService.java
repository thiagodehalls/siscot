package br.com.sicot.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.sicot.data.RelatorioClienteListProducer;
import br.com.sicot.model.RelatorioUltimaCompar;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/relatorioclienteultimacompra")
@RequestScoped
public class RelatorioUltimaComparRESTService {
   @Inject
   private RelatorioClienteListProducer producer;

   @GET
   @Produces("application/json")
   @Path(value="/cliente/{id}")
   public List<RelatorioUltimaCompar> list(@PathParam(value="id") String id) {
	  List<RelatorioUltimaCompar> valores = producer.valorePagosDozeMes(id);
	  
	  for (RelatorioUltimaCompar relatorioDozeUltimaCompar : valores) {
		relatorioDozeUltimaCompar.getMes();
	  }
	  
      return valores;
   }
}
