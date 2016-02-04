package br.com.sicot.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;

/**
 * Created by thiago on 29/12/15.
 */
public class ItemListaCompraPK implements Serializable {
    private Long produtoId;
    private Long listaCompraId;

    @Id
    @Column(name = "produto_id", nullable = false, insertable = true, updatable = true)
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @Id
    @Column(name = "lista_compra_id", nullable = false, insertable = true, updatable = true)
    public Long getListaCompraId() {
        return listaCompraId;
    }

    public void setListaCompraId(Long listaCompraId) {
        this.listaCompraId = listaCompraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemListaCompraPK that = (ItemListaCompraPK) o;

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
