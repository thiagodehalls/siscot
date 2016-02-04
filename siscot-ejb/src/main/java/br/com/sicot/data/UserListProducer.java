package br.com.sicot.data;

import br.com.sicot.model.MarcaFabricante;
import br.com.sicot.model.User;
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
public class UserListProducer {
   @Inject
   private Resources resource;

   private List<User> users;

   @Produces
   @Named
   public List<User> getUsers() {
      return users;
   }

   public void onUserListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final User user) {
      allUserOrderedByNome();
   }

   @PostConstruct
   public void allUserOrderedByNome() {
      CriteriaBuilder cb = resource.produceCriteriaBuilder();
      CriteriaQuery<User> criteria = cb.createQuery(User.class);
      Root<User> user = criteria.from(User.class);

      criteria.select(user).orderBy(cb.asc(user.get("nome")));
      users = resource.getResultList(criteria);
   }
}
