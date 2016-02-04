package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thiago on 29/12/15.
 */
@Entity
@XmlRootElement
@Table(name = "lista_compra")
@SequenceGenerator(name = "lista_compra_seq", sequenceName = "lista_compra_lista_compra_id_seq", allocationSize = 1, initialValue = 1)
public class ListaCompra implements Serializable{

    private Long id;
    private String nome;
    private Cliente cliente;
    private List<ItemListaCompra> itemsListaCompra;

    @Id
    @Column(name = "lista_compra_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lista_compra_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "lista_compra_nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cliente_cpf", referencedColumnName = "cliente_cpf")
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @OneToMany(mappedBy = "listaCompra", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    public List<ItemListaCompra> getItemsListaCompra() {
        return itemsListaCompra;
    }

    public void setItemsListaCompra(List<ItemListaCompra> itemsListaCompra) {
        this.itemsListaCompra = itemsListaCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListaCompra that = (ListaCompra) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
