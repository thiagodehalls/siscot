package br.com.sicot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.sicot.model.Cliente;
import br.com.sicot.model.Compra;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.util.Resources;

@Stateless
public class HistoricoVendaEmpresaProducer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Resources resource;

	@PostConstruct
	public void init() {
	}

	public List<Compra> findHistoricoCompra(Compra compra) {
		CriteriaBuilder cb = resource.produceCriteriaBuilder();
		CriteriaQuery<Compra> criteria = cb.createQuery(Compra.class);
		Root<Compra> root = criteria.from(Compra.class);
		root.fetch("itemCompras", JoinType.INNER);
		Join<Compra, ItemCompra> item = root.join("itemCompras", JoinType.INNER);

		Predicate[] predicates = getSearchPredicates(root, item, compra);

		@SuppressWarnings("unchecked")
		TypedQuery<Compra> query = (TypedQuery<Compra>) resource.produceTypedQuery(
				criteria.select(root).where(predicates).distinct(true).orderBy(cb.desc(root.get("dataHora"))));

		return query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Compra> root, Join<Compra, ItemCompra> item, Compra compra) {
		CriteriaBuilder builder = resource.produceCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		predicatesList.add(builder.equal(item.<Cliente>get("mercadoCnpj"), compra.getCnpj()));
		
		if(compra.getDataInicio()!=null && compra.getDataFim()!=null){
			predicatesList.add(builder.between(root.<Date> get("dataHora"), compra.getDataInicio(), compra.getDataFim()));
		}
		if(compra.getSituacao()!=null && !compra.getSituacao().isEmpty()){
			predicatesList.add(builder.equal(root.<String> get("situacao"), compra.getSituacao()));
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}
}
