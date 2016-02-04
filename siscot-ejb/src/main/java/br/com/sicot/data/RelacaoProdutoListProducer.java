package br.com.sicot.data;

import br.com.sicot.model.*;
import br.com.sicot.util.Resources;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by thiago on 03/12/15.
 */
@Stateless
public class RelacaoProdutoListProducer implements Serializable {

    @Inject
    private Resources resource;
    private Long filterRelacaoProduto;


    @PostConstruct
    public void init() {

    }

    public List<Produto> findProdutoOrderByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        Subquery subCriteria = criteria.subquery(RelacaoProduto.class);
        Root subRootEntity = subCriteria.from(RelacaoProduto.class);
        subCriteria.select(subRootEntity.get("produtoIdRelacao"));

        Predicate correlatePredicate = cb.equal(subRootEntity.get("produtoId"), this.filterRelacaoProduto);
        subCriteria.where(correlatePredicate);

        criteria.where(cb.not(cb.in(root.get("id")).value(subCriteria)));

        TypedQuery<Produto> query = (TypedQuery<Produto>) resource.produceTypedQuery(criteria.orderBy(cb.asc(root.get("nome"))));

        return query.getResultList();
    }

    public List<RelacaoProduto> findRelacaoProduto() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<RelacaoProduto> criteria = cb.createQuery(RelacaoProduto.class);
        Root<RelacaoProduto> root = criteria.from(RelacaoProduto.class);

        Predicate[] predicates = getSearchPredicates(root);

        TypedQuery<RelacaoProduto> query = (TypedQuery<RelacaoProduto>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .where(predicates));

        return query.getResultList();
    }


    private Predicate[] getSearchPredicates(Root<RelacaoProduto> root) {

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        Long id = this.filterRelacaoProduto;
        if (id != null) {
            predicatesList.add(builder.equal(root.get("produtoId"), id));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public Long getFilterRelacaoProduto() {
        return filterRelacaoProduto;
    }

    public void setFilterRelacaoProduto(Long filterRelacaoProduto) {
        this.filterRelacaoProduto = filterRelacaoProduto;
    }
}
