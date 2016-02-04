package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@Table(name = "item_compra")
@IdClass(ItemCompraPK.class)
public class ItemCompra implements Serializable{
    private BigDecimal valorUnitario;
    private BigDecimal quantidade;
    private BigDecimal valorObtidoDesconto;
    private BigDecimal valorAcima;
    private BigDecimal valor;
    private Long compraId;
    private String mercadoCnpj;
    private Long produtoId;
    private Compra compra;
    private Empresa empresa;
    private Produto produto;
    private String situacao;

    @NotNull
    @Column(name = "item_compra_valor_unitario", insertable = true, updatable = true)
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @NotNull
    @Column(name = "item_compra_quantidade", insertable = true, updatable = true)
    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    @NotNull
    @Column(name = "valor_obtido_desconto", insertable = true, updatable = true)
    public BigDecimal getValorObtidoDesconto() {
        if(getProduto().getPrecoMedio()!=null && getValorUnitario()!=null){
            setValorObtidoDesconto(getProduto().getPrecoMedio().subtract(getValorUnitario()));
        }
        return valorObtidoDesconto;
    }

    public void setValorObtidoDesconto(BigDecimal valorObtidoDesconto) {
        this.valorObtidoDesconto = valorObtidoDesconto;
    }

    @NotNull
    @Column(name = "media_valores_acima", insertable = true, updatable = true)
    public BigDecimal getValorAcima() {
        if(getProduto().getPrecoMedio()!=null){
            setValorAcima(getProduto().getPrecoMedio());
        }
        return valorAcima;
    }

    public void setValorAcima(BigDecimal valorAcima) {
        this.valorAcima = valorAcima;
    }

    @Id
    @NotNull
    @Column(name = "compra_id", nullable = false, insertable = true)
    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    @Id
    @NotNull
    @Column(name = "mercado_cnpj",  insertable = true, updatable = true)
    public String getMercadoCnpj() {
        return mercadoCnpj;
    }

    public void setMercadoCnpj(String mercadoCnpj) {
        this.mercadoCnpj = mercadoCnpj;
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

    @ManyToOne
    @JoinColumn(name = "compra_id", referencedColumnName = "compra_id", insertable = false, updatable = false)
    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @ManyToOne
    @JoinColumn(name = "mercado_cnpj", referencedColumnName = "mercado_cnpj", insertable = false, updatable = false)
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id", insertable = false, updatable = false)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    @NotNull
    @Size(max = 1)
    @Column(name = "item_situacao")
    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Transient
    public BigDecimal getValor() {

        if (getValorUnitario()!=null && getQuantidade()!=null) {
            setValor(getValorUnitario().multiply(getQuantidade()));
        }

        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemCompra that = (ItemCompra) o;

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
