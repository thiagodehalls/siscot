package br.com.sicot.data;

import br.com.sicot.model.*;
import br.com.sicot.util.Resources;
import org.hibernate.validator.HibernateValidatorConfiguration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by thiago on 03/12/15.
 */
@Named
@RequestScoped
public class ProdutoListProducer {

    @Inject
    private Resources resource;

    @Produces
    @Named
    private List<Produto> produtos;

    @Produces
    @Named
    private Produto filterProduto;

    public List<Produto> getProdutos() {
        return produtos;
    }

    @PostConstruct
    public void init() {
        filterProduto = new Produto();
        filterProduto.setCategoria(new Categoria());
        filterProduto.setUnidadeMedida(new UnidadeMedida());
        filterProduto.setMarcaFabricante(new MarcaFabricante());
        produtos = new ArrayList<Produto>();
    }

    public void findProdutoOrderByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Produto> criteria = cb.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        Predicate[] predicates = getSearchPredicates(root);

        TypedQuery<Produto> query = (TypedQuery<Produto>) resource.
                produceTypedQuery(criteria
                                    .select(root)
                                        .where(predicates)
                                            .orderBy(cb.asc(root.get("nome"))));

        produtos = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Produto> root) {

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        Long id = this.filterProduto.getId();
        if (id != null) {
            predicatesList.add(builder.equal(root.get("id"), id));
        }
        String nome = this.filterProduto.getNome();
        if (nome != null && !nome.isEmpty()) {
            predicatesList.add(builder.like(builder.upper(root.<String>get("nome")), "%" + nome.toUpperCase() + "%"));
        }
        String descricao = this.filterProduto.getDescricao();
        if (descricao != null && !descricao.isEmpty()) {
            predicatesList.add(builder.like(builder.upper(root.<String>get("descricao")), "%" + descricao.toUpperCase() + "%"));
        }
        BigDecimal medida = this.filterProduto.getMedida();
        if (medida != null) {
            predicatesList.add(builder.equal(root.get("medida"), medida));
        }
        String palavrasChave = this.filterProduto.getPalavrasChave();
        if (palavrasChave != null && !palavrasChave.isEmpty()) {
            predicatesList.add(builder.like(builder.upper(root.<String>get("palavrasChave")), "%" + palavrasChave.toUpperCase() + "%"));
        }
        Categoria categoria = this.filterProduto.getCategoria();
        if (categoria != null && categoria.getId()!=null && categoria.getId()!=0) {
            predicatesList.add(builder.equal(root.get("categoria"), categoria));
        }
        MarcaFabricante marcaFabricante = this.filterProduto.getMarcaFabricante();
        if (marcaFabricante != null && marcaFabricante.getId()!=null && marcaFabricante.getId()!=0) {
            predicatesList.add(builder.equal(root.get("marcaFabricante"), marcaFabricante));
        }
        UnidadeMedida unidadeMedida = this.filterProduto.getUnidadeMedida();
        if (unidadeMedida != null && unidadeMedida.getId()!=null && unidadeMedida.getId()!=0) {
            predicatesList.add(builder.equal(root.get("unidadeMedida"), unidadeMedida));
        }
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
