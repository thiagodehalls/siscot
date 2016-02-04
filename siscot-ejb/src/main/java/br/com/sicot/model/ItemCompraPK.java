package br.com.sicot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by thiago on 20/11/15.
 */
public class ItemCompraPK implements Serializable {
    private Long compraId;
    private String mercadoCnpj;
    private Long produtoId;

    @Column(name = "compra_id", nullable = false, insertable = true, updatable = true)
    @Id
    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    @Column(name = "mercado_cnpj", nullable = false, insertable = true, updatable = true, length = 19)
    @Id
    public String getMercadoCnpj() {
        return mercadoCnpj;
    }

    public void setMercadoCnpj(String mercadoCnpj) {
        this.mercadoCnpj = mercadoCnpj;
    }

    @Column(name = "produto_id", nullable = false, insertable = true, updatable = true)
    @Id
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemCompraPK that = (ItemCompraPK) o;

        if (compraId != null ? !compraId.equals(that.compraId) : that.compraId != null) return false;
        if (mercadoCnpj != null ? !mercadoCnpj.equals(that.mercadoCnpj) : that.mercadoCnpj != null) return false;
        if (produtoId != null ? !produtoId.equals(that.produtoId) : that.produtoId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = compraId != null ? compraId.hashCode() : 0;
        result = 31 * result + (mercadoCnpj != null ? mercadoCnpj.hashCode() : 0);
        result = 31 * result + (produtoId != null ? produtoId.hashCode() : 0);
        return result;
    }
}
