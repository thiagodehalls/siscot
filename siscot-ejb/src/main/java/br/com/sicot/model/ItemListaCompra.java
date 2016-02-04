package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by thiago on 29/12/15.
 */
@Entity
@Table(name = "item_lista_compra")
@IdClass(ItemListaCompraPK.class)
public class ItemListaCompra {

    private BigDecimal quantidade;
    private Long produtoId;
    private Long listaCompraId;
    private Produto produto;
    private ListaCompra listaCompra;

    @Id
    @NotNull
    @Column(name = "lista_compra_id", insertable = true, updatable = true)
    public Long getListaCompraId() {
        return listaCompraId;
    }

    public void setListaCompraId(Long listaCompraId) {
        this.listaCompraId = listaCompraId;
    }

    @Id
    @NotNull
    @Column(name = "produto_id", insertable = true, updatable = true)
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @NotNull
    @Column(name = "item_lista_compra_quantidade", insertable = true, updatable = true)
    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    @ManyToOne
    @JoinColumn(name = "lista_compra_id", referencedColumnName = "lista_compra_id", insertable = false, updatable = false)
    public ListaCompra getListaCompra() {
        return listaCompra;
    }

    public void setListaCompra(ListaCompra listaCompra) {
        this.listaCompra = listaCompra;
    }

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id", insertable = false, updatable = false)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemListaCompra that = (ItemListaCompra) o;

        if (listaCompraId != null ? !listaCompraId.equals(that.listaCompraId) : that.listaCompraId != null)
            return false;
        if (produtoId != null ? !produtoId.equals(that.produtoId) : that.produtoId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = produtoId != null ? produtoId.hashCode() : 0;
        result = 31 * result + (listaCompraId != null ? listaCompraId.hashCode() : 0);
        return result;
    }
}
