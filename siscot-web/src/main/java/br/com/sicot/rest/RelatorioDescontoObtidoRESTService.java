package br.com.sicot.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.sicot.data.RelatorioClienteListProducer;
import br.com.sicot.model.DescontoObtido;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/relatorioclientedescontoobtido")
@RequestScoped
public class RelatorioDescontoObtidoRESTService {
   @Inject
   private RelatorioClienteListProducer producer;

   @GET
   @Produces("application/json")
   @Path(value="/cliente/{id}")
   public List<DescontoObtido> listDescontos(@PathParam(value="id") String id) {
	  List<DescontoObtido> valores = producer.valoreDescontoSeisMes(id);
	  
	  for (DescontoObtido descontoObtido : valores) {
		  descontoObtido.getMes();
	  }
	  
      return valores;
   }

   @GET
   @Produces("application/json")
   @Path(value="/cliente/totaldesconto/{id}")
   public DescontoObtido totalDesconto(@PathParam(value="id") String id) {
	  
      return producer.valoreTotalDesconto(id);
   }
   
   @GET
   @Produces("application/json")
   @Path(value="/cliente/totalcompra/{id}")
   public DescontoObtido totalCompra(@PathParam(value="id") String id) {
	  
      return producer.valoreTotalCompra(id);
   }
   
}


