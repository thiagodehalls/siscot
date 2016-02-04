package br.com.sicot.data;

import br.com.sicot.model.Atributo;
import br.com.sicot.util.Resources;

import java.util.List;
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

@RequestScoped
public class AtributoListProducer {

   @Inject
   private Resources resource;

   private List<Atributo> atributos;

   @Produces
   @Named
   public List<Atributo> getAtributos() {
      return atributos;
   }

   public void onAtributoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Atributo atributo) {
      allAtributoOrderedByNome();
   }

   @PostConstruct
   public void allAtributoOrderedByNome() {
      CriteriaBuilder cb = resource.produceCriteriaBuilder();
      CriteriaQuery<Atributo> criteria = cb.createQuery(Atributo.class);
      Root<Atributo> atributo = criteria.from(Atributo.class);

      criteria.select(atributo).orderBy(cb.asc(atributo.get("nome")));
      atributos = resource.getResultList(criteria);
   }
}
