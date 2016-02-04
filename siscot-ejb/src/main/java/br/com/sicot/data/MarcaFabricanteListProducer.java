package br.com.sicot.data;

import br.com.sicot.model.Atributo;
import br.com.sicot.model.MarcaFabricante;
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
public class MarcaFabricanteListProducer {
   @Inject
   private Resources resource;

   private List<MarcaFabricante> marcaFabricantes;

   @Produces
   @Named
   public List<MarcaFabricante> getMarcaFabricantes() {
      return marcaFabricantes;
   }

   public void onMarcaFabricanteListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final MarcaFabricante marcaFabricante) {
      allMarcaFabricanteOrderedByNome();
   }

   @PostConstruct
   public void allMarcaFabricanteOrderedByNome() {
      CriteriaBuilder cb = resource.produceCriteriaBuilder();
      CriteriaQuery<MarcaFabricante> criteria = cb.createQuery(MarcaFabricante.class);
      Root<MarcaFabricante> marcaFabricante = criteria.from(MarcaFabricante.class);

      criteria.select(marcaFabricante).orderBy(cb.asc(marcaFabricante.get("nome")));
      marcaFabricantes = resource.getResultList(criteria);
   }
}
