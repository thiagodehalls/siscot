package br.com.sicot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by thiago on 20/11/15.
 */
public class RelacaoProdutoPK implements Serializable {
    private Long produtoIdRelacao;
    private Long produtoId;

    @Column(name = "produto_id_relacao", nullable = false, insertable = true, updatable = true)
    @Id
    public Long getProdutoIdRelacao() {
        return produtoIdRelacao;
    }

    public void setProdutoIdRelacao(Long produtoIdRelacao) {
        this.produtoIdRelacao = produtoIdRelacao;
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

        RelacaoProdutoPK that = (RelacaoProdutoPK) o;

        if (produtoId != null ? !produtoId.equals(that.produtoId) : that.produtoId != null) return false;
        if (produtoIdRelacao != null ? !produtoIdRelacao.equals(that.produtoIdRelacao) : that.produtoIdRelacao != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = produtoIdRelacao != null ? produtoIdRelacao.hashCode() : 0;
        result = 31 * result + (produtoId != null ? produtoId.hashCode() : 0);
        return result;
    }
}
