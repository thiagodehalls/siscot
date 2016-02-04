package br.com.sicot.data;

import br.com.sicot.model.*;
import br.com.sicot.util.Resources;

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
import java.util.List;

/**
 * Created by thiago on 03/12/15.
 */
@Named
@RequestScoped
public class EmpresaListProducer {

    @Inject
    private Resources resource;

    @Produces
    @Named
    private List<Empresa> empresas;

    @Produces
    @Named
    private Empresa filterEmpresa;

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    @PostConstruct
    public void init() {
        filterEmpresa = new Empresa();
        empresas = new ArrayList<Empresa>();
    }

    public void findEmpresaOrderByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Empresa> criteria = cb.createQuery(Empresa.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        Predicate[] predicates = getSearchPredicates(root);

        TypedQuery<Empresa> query = (TypedQuery<Empresa>) resource.
                produceTypedQuery(criteria
                                    .select(root)
                                        .where(predicates)
                                            .orderBy(cb.asc(root.get("nomeFantasia"))));

        empresas = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Empresa> root) {

        CriteriaBuilder builder = resource.produceCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String cnpj = this.filterEmpresa.getCnpj();
        if (cnpj != null &&  !cnpj.isEmpty()){
            predicatesList.add(builder.equal(root.get("cnpj"), cnpj));
        }
        String nome = this.filterEmpresa.getNomeFantasia();
        if (nome != null && !nome.isEmpty()) {
            predicatesList.add(builder.like(builder.upper(root.<String>get("nomeFantasia")), "%" + nome.toUpperCase() + "%"));
        }
        String razaoSocial = this.filterEmpresa.getRazaoSocial();
        if (razaoSocial != null && !razaoSocial.isEmpty()) {
            predicatesList.add(builder.like(builder.upper(root.<String>get("razaoSocial")), "%" + razaoSocial.toUpperCase() + "%"));
        }
        String email = this.filterEmpresa.getEmail();
        if (email != null && !email.isEmpty()) {
            predicatesList.add(builder.equal(root.<String>get("email"), email.toUpperCase()));
        }
        String situacao = this.filterEmpresa.getSituacao();
        if (situacao != null && !situacao.isEmpty()) {
            predicatesList.add(builder.equal(root.<String>get("situacao"), situacao.toUpperCase()));
        }
        Long cidade = this.filterEmpresa.getCidade();
        if (cidade != null && cidade!=0L) {
            predicatesList.add(builder.equal(root.get("cidade"), cidade));
        }
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
