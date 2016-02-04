package br.com.sicot.data;

import br.com.sicot.model.*;
import br.com.sicot.util.Resources;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiago on 14/12/15.
 */
@Stateless
public class CompraLitProducer implements Serializable {

    @Inject
    private Resources resource;
    private Produto filterProduto = new Produto();

    @PostConstruct
    public void init() {
    }

    public List<Produto> findProdutoOrderByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        TypedQuery<Produto> query = (TypedQuery<Produto>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .orderBy(cb.asc(root.get("nome"))));

        return query.getResultList();
    }


    public List<Produto> findProdutoDestaqueOrderByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        TypedQuery<Produto> query = (TypedQuery<Produto>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .where(cb.equal(root.<String>get("destaque"), "S"))
                        .orderBy(cb.asc(root.get("nome"))));

        return query.getResultList();
    }

    public List<Produto> findProdutoOrderByNome(List<String> filtros) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        Predicate[] predicates = getSearchPredicates(root, filtros);

        TypedQuery<Produto> query = (TypedQuery<Produto>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .where(predicates)
                        .orderBy(cb.asc(root.get("nome"))));

        return query.getResultList();
    }

    public List<Compra> findCompraPendenteOrderByData(Cliente cliente) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Compra> criteria = cb.createQuery(Compra.class);
        Root<Compra> root = criteria.from(Compra.class);
        root.fetch("itemCompras", JoinType.INNER);

        Predicate[] predicates = getSearchPredicates(root, cliente);

        TypedQuery<Compra> query = (TypedQuery<Compra>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .distinct(true)
                        .where(predicates)
                        .orderBy(cb.desc(root.get("dataHora"))));

        return query.getResultList();
    }

    public List<Compra> findCompraPendenteOrderByData(Empresa empresa) {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Compra> criteria = cb.createQuery(Compra.class);
        Root<Compra> root = criteria.from(Compra.class);
        root.fetch("itemCompras", JoinType.INNER);
        Join<Compra, ItemCompra> item = root.join("itemCompras", JoinType.INNER);
        
        Predicate[] predicates = getSearchPredicates(root, item, empresa);

        TypedQuery<Compra> query = (TypedQuery<Compra>) resource.
                produceTypedQuery(criteria
                        .select(root)
                        .distinct(true)
                        .where(predicates)
                        .orderBy(cb.desc(root.get("dataHora"))));

        return query.getResultList();
    }
    
    private Predicate[] getSearchPredicates(Root<Compra> root, Join<Compra, ItemCompra> iten, Empresa empresa) {
        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        predicatesList.add(builder.equal(iten.<Cliente>get("mercadoCnpj"), empresa.getCnpj()));
        predicatesList.add(builder.equal(root.<String>get("situacao"), "P"));
        predicatesList.add(builder.equal(iten.<String>get("situacao"), "P"));
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
    
    private Predicate[] getSearchPredicates(Root<Compra> root, Cliente cliente) {
        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        predicatesList.add(builder.equal(root.<Cliente>get("cliente").get("cpf"), cliente.getCpf()));
        predicatesList.add(builder.equal(root.<String>get("situacao"), "P"));
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    private Predicate[] getSearchPredicates(Root<Produto> root, List<String> filtros) {

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        for (String filtro : filtros){
            predicatesList.add(
                    builder.or(
                        builder.like(builder.upper(root.<String>get("nome")), "%" + filtro.trim().toUpperCase() + "%"),
                        builder.like(builder.upper(root.<String>get("descricao")), "%" + filtro.trim().toUpperCase() + "%"),
                        builder.or(builder.like(builder.upper(root.<String>get("palavrasChave")), "%" + filtro.trim().toUpperCase() + "%"))
                    )
            );
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
