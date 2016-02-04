package br.com.sicot.data;

import br.com.sicot.model.MarcaFabricante;
import br.com.sicot.model.UnidadeMedida;
import br.com.sicot.util.Resources;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@RequestScoped
public class UnidadeMedidaListProducer {
   @Inject
   private Resources resource;

   private List<UnidadeMedida> unidadeMedidas;

   @Produces
   @Named
   public List<UnidadeMedida> getUnidadeMedidas() {
      return unidadeMedidas;
   }

   public void onUnidadeMedidaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final UnidadeMedida unidadeMedida) {
      allUnidadeMedidaOrderedByNome();
   }

   @PostConstruct
   public void allUnidadeMedidaOrderedByNome() {
      CriteriaBuilder cb = resource.produceCriteriaBuilder();
      CriteriaQuery<UnidadeMedida> criteria = cb.createQuery(UnidadeMedida.class);
      Root<UnidadeMedida> unidadeMedida = criteria.from(UnidadeMedida.class);

      criteria.select(unidadeMedida).orderBy(cb.asc(unidadeMedida.get("nome")));
      unidadeMedidas = resource.getResultList(criteria);
   }
}
