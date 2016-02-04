package br.com.sicot.data;

import br.com.sicot.model.Categoria;
import br.com.sicot.model.Cliente;
import br.com.sicot.model.ListaCompra;
import br.com.sicot.util.Resources;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ListaCompraListProducer implements Serializable {
   @Inject
   private Resources resource;

   public List<ListaCompra> allListaCompraByClienteOrderedByNome(Cliente cliente) {
      CriteriaBuilder cb = resource.produceCriteriaBuilder();
      CriteriaQuery<ListaCompra> criteria = cb.createQuery(ListaCompra.class);
      Root<ListaCompra> listaCompra = criteria.from(ListaCompra.class);
      listaCompra.fetch("itemsListaCompra", JoinType.LEFT);

      criteria
          .select(listaCompra)
          .distinct(true)
          .where(cb.equal(listaCompra.<Cliente>get("cliente").get("cpf"), cliente.getCpf()))
          .orderBy(cb.asc(listaCompra.get("nome")));
      return resource.getResultList(criteria);
   }

    public List<Categoria> allCategoriaEagerOrderedByNome() {
        CriteriaBuilder cb = resource.produceCriteriaBuilder();
        CriteriaQuery<Categoria> criteria = cb.createQuery(Categoria.class);
        Root<Categoria> categoria = criteria.from(Categoria.class);
        categoria.fetch("produtos");

        criteria.select(categoria).distinct(true).orderBy(cb.asc(categoria.get("nome")));
        return resource.getResultList(criteria);
    }
}
