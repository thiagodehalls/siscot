package br.com.sicot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.sicot.model.Cliente;
import br.com.sicot.model.Empresa;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.util.Resources;

@Stateless
public class ItemCompraListProducer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
    private Resources resource;
	
	public List<ItemCompra> findCompraPendenteOrderByData(Empresa empresa, Integer compraId) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<ItemCompra> criteria = cb.createQuery(ItemCompra.class);
        Root<ItemCompra> root = criteria.from(ItemCompra.class);

        Predicate[] predicates = getSearchPredicates(root, empresa, compraId, "P");

        TypedQuery<ItemCompra> query = (TypedQuery<ItemCompra>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .distinct(true)
                        .where(predicates));

        return query.getResultList();
    }
	
	public List<ItemCompra> findCompraOrderByData(Empresa empresa, Integer compraId) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<ItemCompra> criteria = cb.createQuery(ItemCompra.class);
        Root<ItemCompra> root = criteria.from(ItemCompra.class);

        Predicate[] predicates = getSearchPredicates(root, empresa, compraId, null);

        TypedQuery<ItemCompra> query = (TypedQuery<ItemCompra>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .distinct(true)
                        .where(predicates));

        return query.getResultList();
    }
	
	public List<ItemCompra> findCompraPendenteOrderByData(Empresa empresa) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<ItemCompra> criteria = cb.createQuery(ItemCompra.class);
        Root<ItemCompra> root = criteria.from(ItemCompra.class);

        Predicate[] predicates = getSearchPredicates(root, empresa);

        TypedQuery<ItemCompra> query = (TypedQuery<ItemCompra>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .distinct(true)
                        .where(predicates));

        return query.getResultList();
    }

	private Predicate[] getSearchPredicates(Root<ItemCompra> root, Empresa empresa, Integer compraId, String situacao) {
        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        predicatesList.add(builder.equal(root.<Cliente>get("mercadoCnpj"), empresa.getCnpj()));

        if(situacao!=null && !situacao.isEmpty()){
        	predicatesList.add(builder.equal(root.<String>get("situacao"), "P"));
        }
        
        predicatesList.add(builder.equal(root.get("compraId"), compraId));
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
	
	private Predicate[] getSearchPredicates(Root<ItemCompra> root, Empresa empresa) {
        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        predicatesList.add(builder.equal(root.<Cliente>get("mercadoCnpj"), empresa.getCnpj()));
        predicatesList.add(builder.equal(root.<String>get("situacao"), "P"));
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
