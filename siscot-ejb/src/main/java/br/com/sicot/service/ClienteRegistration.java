package br.com.sicot.service;

import br.com.sicot.model.Cliente;
import br.com.sicot.model.Produto;
import br.com.sicot.model.Roles;
import br.com.sicot.model.User;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.management.relation.Role;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ClienteRegistration implements Serializable{

   @Inject
   private Logger log;

   @Inject
   private Resources resource;

   @Inject
   private Event<Cliente> clienteEventSrc;

   public void register(Cliente cliente) throws Exception {
      log.info("Registrando " + cliente.getNome());
      if (this.findById(cliente.getCpf())==null){
          resource.insert(cliente);

          User user = new User();
          user.setEmail(cliente.getEmail());
          user.setNome(cliente.getNome());
          user.setId(cliente.getEmail());
          user.setPassword(cliente.getPassword());
          resource.insert(user);

          Roles roles = new Roles();
          roles.setUser(cliente.getEmail());
          roles.setRole(Roles.CLIENTE);
          roles.setGroup(Roles.CLIENTE);
          resource.insert(roles);
      }else{
          resource.update(cliente);
      }

       clienteEventSrc.fire(cliente);
   }

   public Cliente findById(String id) throws Exception {
       log.info("Find by id " + id);
       return (Cliente) resource.findById(Cliente.class, id);
   }

   public Cliente findByEmail(String email) throws Exception {
       log.info("Find by email " + email);

       CriteriaBuilder cb = resource.produceCriteriaBuilder();
       CriteriaQuery<Cliente> criteria = cb.createQuery(Cliente.class);
       Root<Cliente> root = criteria.from(Cliente.class);

       criteria.select(root).where(cb.equal(root.get("email"), email));

       return (Cliente) resource.getSingleResult(criteria);
   }

   public void remove(Cliente cliente) throws Exception {
        resource.remove(Cliente.class, cliente.getCpf());
    }
}
