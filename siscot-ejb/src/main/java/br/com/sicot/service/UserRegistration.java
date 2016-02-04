package br.com.sicot.service;

import br.com.sicot.model.Categoria;
import br.com.sicot.model.Roles;
import br.com.sicot.model.User;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class UserRegistration implements Serializable{

   @Inject
   private Logger log;

   @Inject
   private Resources resource;

   @Inject
   private Event<User> userEventSrc;

   public void register(User user) throws Exception {
      log.info("Registrando " + user.getNome());
      if (user.getId()==null){
          resource.insert(user);

          Roles roles = new Roles();
          roles.setUser(user.getId());
          roles.setRole(Roles.ADMINISTRADOR);
          roles.setGroup(Roles.ADMINISTRADOR);
          resource.insert(roles);
      }else{
          resource.update(user);
      }

       userEventSrc.fire(user);
   }

   public User findById(String id) throws Exception {
       log.info("Find by id " + id);
       return (User) resource.findById(User.class, id);
   }

   public boolean isAdmin(User user){
       log.info("Find roles " + user);

       CriteriaBuilder cb = resource.produceCriteriaBuilder();
       CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
       Root<Roles> role = criteria.from(Roles.class);

       CriteriaBuilder builder = resource.produceCriteriaBuilder();
       List<Predicate> predicatesList = new ArrayList<Predicate>();
       predicatesList.add(builder.equal(role.<String>get("user"), user.getId()));
       predicatesList.add(builder.equal(role.<String>get("role"), Roles.ADMINISTRADOR));

       criteria.select(role).where(predicatesList.toArray(new Predicate[predicatesList.size()]));

       return !resource.getResultList(criteria).isEmpty();
   }

    public boolean isEmpresa(User user){
        log.info("Find roles " + user);

        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
        Root<Roles> role = criteria.from(Roles.class);

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        predicatesList.add(builder.equal(role.<String>get("user"), user.getId()));
        predicatesList.add(builder.equal(role.<String>get("role"), Roles.EMPRESA));

        criteria.select(role).where(predicatesList.toArray(new Predicate[predicatesList.size()]));

        return !resource.getResultList(criteria).isEmpty();
    }

    public boolean isCliente(User user){
        log.info("Find roles " + user);

        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
        Root<Roles> role = criteria.from(Roles.class);

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        predicatesList.add(builder.equal(role.<String>get("user"), user.getId()));
        predicatesList.add(builder.equal(role.<String>get("role"), Roles.CLIENTE));

        criteria.select(role).where(predicatesList.toArray(new Predicate[predicatesList.size()]));

        return !resource.getResultList(criteria).isEmpty();
    }

   public void remove(User user) throws Exception {
        resource.remove(User.class, user.getId());
   }
}
