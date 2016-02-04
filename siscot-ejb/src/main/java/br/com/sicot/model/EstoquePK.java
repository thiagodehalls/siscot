package br.com.sicot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by thiago on 20/11/15.
 */
public class EstoquePK implements Serializable {
    private String cnpj;
    private Long produtoId;

    @Id
    @Column(name = "mercado_cnpj", nullable = false, insertable = true, updatable = true)
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Id
    @Column(name = "produto_id", nullable = false, insertable = true, updatable = true)
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

        EstoquePK estoquePK = (EstoquePK) o;

        if (cnpj != null ? !cnpj.equals(estoquePK.cnpj) : estoquePK.cnpj != null) return false;
        if (produtoId != null ? !produtoId.equals(estoquePK.produtoId) : estoquePK.produtoId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cnpj != null ? cnpj.hashCode() : 0;
        result = 31 * result + (produtoId != null ? produtoId.hashCode() : 0);
        return result;
    }
}
