package br.com.sicot.util;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class Resources implements Serializable{

    @SuppressWarnings("unused")
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    public CriteriaBuilder produceCriteriaBuilder() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        return cb;
    }

    public List getResultList(CriteriaQuery criteria){
        return em.createQuery(criteria).getResultList();
    }

    public Object getSingleResult(CriteriaQuery criteria){
        return em.createQuery(criteria).getSingleResult();
    }

    public TypedQuery produceTypedQuery(CriteriaQuery criteria){
        return em.createQuery(criteria);
    }

    public void insert(Object obj){
        em.persist(obj);
    }

    public Object findById(Class clazz, Serializable id){
        return  em.find(clazz, id);
    }

    public List findAll(Class clazz){
        return em.createQuery("SELECT t FROM " + clazz.getSimpleName() + " t").getResultList();
    }

    public Query createQuery(String namedQuery, Class clazz){
    	return em.createNamedQuery(namedQuery);
    }
    
    public void remove(Class clazz, Serializable id){
        Object object = em.find(clazz, id);
        if (object!=null) {
            em.remove(object);
        }
    }

    public void update(Object obj) {
        em.merge(obj);
    }
}
