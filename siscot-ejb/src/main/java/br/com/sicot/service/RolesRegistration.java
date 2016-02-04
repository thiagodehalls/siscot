package br.com.sicot.service;

import br.com.sicot.model.Roles;
import br.com.sicot.model.User;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class RolesRegistration implements Serializable{

   @Inject
   private Logger log;

   @Inject
   private Resources resource;

   @Inject
   private Event<Roles> rolesEventSrc;

   public void register(Roles roles) throws Exception {
      log.info("Registrando " + roles.getRole());
      resource.insert(roles);
      rolesEventSrc.fire(roles);
   }

}
