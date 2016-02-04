package br.com.sicot.model;

import javax.persistence.*;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@Table(name = "relacao_produto")
@IdClass(RelacaoProdutoPK.class)
public class RelacaoProduto {
    private Long produtoIdRelacao;
    private Long produtoId;
    private Produto produto;
    private Produto produtoRelacao;

    public RelacaoProduto() {
    }

    public RelacaoProduto(Long produtoId, Long produtoIdRelacao) {
        this.produtoId = produtoId;
        this.produtoIdRelacao = produtoIdRelacao;
    }

    @Id
    @Column(name = "produto_id_relacao", nullable = false, insertable = true, updatable = true)
    public Long getProdutoIdRelacao() {
        return produtoIdRelacao;
    }

    public void setProdutoIdRelacao(Long produtoIdRelacao) {
        this.produtoIdRelacao = produtoIdRelacao;
    }

    @Id
    @Column(name = "produto_id", nullable = false, insertable = true, updatable = true)
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id", insertable = false, updatable = false)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @ManyToOne
    @JoinColumn(name = "produto_id_relacao", referencedColumnName = "produto_id", insertable = false, updatable = false)
    public Produto getProdutoRelacao() {
        return produtoRelacao;
    }

    public void setProdutoRelacao(Produto produtoRelacao) {
        this.produtoRelacao = produtoRelacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelacaoProduto that = (RelacaoProduto) o;

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
