package br.com.sicot.service;

import br.com.sicot.model.*;
import br.com.sicot.util.Resources;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by thiago on 04/12/15.
 */
@Stateless
public class EmpresaRegistration implements Serializable{

    @Inject
    private Logger log;

    @Inject
    private Resources resource;

    @Inject
    private Event<Empresa> empresaEventSrc;

    public void register(Empresa empresa) throws Exception {
        log.info("Registrando " + empresa.getNomeFantasia());

        if(findById(empresa.getCnpj())==null) {
            resource.insert(empresa);

            User user = new User();
            user.setEmail(empresa.getEmail());
            user.setNome(empresa.getNomeFantasia());
            user.setId(empresa.getEmail());
            user.setPassword(empresa.getPassword());
            resource.insert(user);

            Roles roles = new Roles();
            roles.setUser(empresa.getEmail());
            roles.setRole(Roles.CLIENTE);
            roles.setGroup(Roles.CLIENTE);
            resource.insert(roles);
        }else{
            resource.update(empresa);
        }

        empresaEventSrc.fire(empresa);
    }

    public Empresa findById(String id) {
        Empresa empresa = (Empresa) resource.findById(Empresa.class, id);
        return empresa;
    }

    public Empresa findByEmail(String email) throws Exception {
        log.info("Find by email " + email);

        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Empresa> criteria = cb.createQuery(Empresa.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        criteria.select(root).where(cb.equal(root.get("email"), email));

        return (Empresa) resource.getSingleResult(criteria);
    }

    public void remove(Empresa empresa){
        resource.remove(Empresa.class, empresa.getCnpj());
    }

}
