package br.com.sicot.data;

import br.com.sicot.model.Categoria;
import br.com.sicot.util.Resources;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@RequestScoped
public class CategoriaListProducer {
    @Inject
    private Resources resource;

    private List<Categoria> categorias;

    @Produces
    @Named
    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void onCategoriaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Categoria categoria) {
        allCategoriaOrderedByNome();
    }

    @PostConstruct
    public void allCategoriaOrderedByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Categoria> criteria = cb.createQuery(Categoria.class);
        Root<Categoria> categoria = criteria.from(Categoria.class);

        criteria.select(categoria).orderBy(cb.asc(categoria.get("nome")));
        categorias = resource.getResultList(criteria);
    }
}
