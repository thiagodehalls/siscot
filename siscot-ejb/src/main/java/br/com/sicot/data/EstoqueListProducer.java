package br.com.sicot.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import br.com.sicot.model.Estoque;
import br.com.sicot.model.Produto;
import br.com.sicot.util.Resources;

/**
 * Created by thiago on 14/12/15.
 */
@Stateless
public class EstoqueListProducer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
    private Resources resource;
    
    private String filterEstoqueEmpresa;

    public List<Produto> findProdutoOrderByNome(String id) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        Subquery subCriteria = criteria.subquery(Estoque.class);
        Root subRootEntity = subCriteria.from(Estoque.class);
        subCriteria.select(subRootEntity.get("produtoId"));

        Predicate correlatePredicate = cb.equal(subRootEntity.get("cnpj"), id);
        subCriteria.where(correlatePredicate);

        criteria.where(cb.not(cb.in(root.get("id")).value(subCriteria)));

        TypedQuery<Produto> query = (TypedQuery<Produto>) resource.produceTypedQuery(criteria.orderBy(cb.asc(root.get("nome"))));

        return query.getResultList();
    }

    public List<Estoque> findEstoqueOrderByProduto(String id) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Estoque> criteria = cb.createQuery(Estoque.class);
        Root<Estoque> root = criteria.from(Estoque.class);

        this.filterEstoqueEmpresa = id;
        Predicate[] predicates = getSearchPredicates(root);

        TypedQuery<Estoque> query = (TypedQuery<Estoque>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .where(predicates));

        return query.getResultList();
    }


    public Estoque findEstoqueProdutoQuantidadeOrderByValorUnitario(Produto produto) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Estoque> criteria = cb.createQuery(Estoque.class);
        Root<Estoque> root = criteria.from(Estoque.class);

        Predicate[] predicates = getSearchPredicatesProdutoQuantidade(root, produto);

        TypedQuery<Estoque> query = (TypedQuery<Estoque>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .where(predicates).orderBy(cb.asc(root.get("valorUnitario"))));

        return query.setFirstResult(0).getSingleResult();
    }

    public Object avgProdutoEstoque(Produto produto) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Object> criteria = cb.createQuery();
        Root<Estoque> root = criteria.from(Estoque.class);

        List<Predicate> predicatesList = new ArrayList<Predicate>();
        predicatesList.add(cb.equal(root.get("produtoId"), produto.getId()));
        Predicate[] predicates = predicatesList.toArray(new Predicate[predicatesList.size()]);

        Expression avgExpression = cb.avg(root.<BigDecimal>get("valorUnitario"));

        TypedQuery<Object> query = (TypedQuery<Object>) resource.
                produceTypedQuery(criteria
                        .select(avgExpression)
                        .where(predicates));

        return query.getSingleResult();
    }



    private Predicate[] getSearchPredicatesProdutoQuantidade(Root<Estoque> root, Produto produto) {
        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();


        predicatesList.add(builder.ge(root.<Number>get("quantidade"), produto.getQuantidade()));
        predicatesList.add(builder.equal(root.get("produtoId"), produto.getId()));

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    private Predicate[] getSearchPredicates(Root<Estoque> root) {

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String cnpj = this.filterEstoqueEmpresa;
        if (cnpj != null && !cnpj.isEmpty()) {
            predicatesList.add(builder.equal(root.get("cnpj"), cnpj));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

}
