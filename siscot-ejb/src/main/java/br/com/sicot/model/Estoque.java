package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@IdClass(EstoquePK.class)
@Table(name = "estoque")
public class Estoque implements Serializable{
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private Date dataInicioPeriodo;
    private Date dataFimPeriodo;
    private String cnpj;
    private String codigo;
    private Long produtoId;
    private Empresa empresa;
    private Produto produto;

    @NotNull
    @Column(name = "produto_codigo")
    public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@NotNull
    @Digits(integer = 9, fraction = 3)
    @Column(name = "estoque_quantidade", insertable = true, updatable = true)
    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    @NotNull
    @Digits(integer = 9, fraction = 3)
    @Column(name = "estoque_valor_unitario", insertable = true, updatable = true)
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estoque_dh_inicio", insertable = true, updatable = true)
    public Date getDataInicioPeriodo() {
        return dataInicioPeriodo;
    }

    public void setDataInicioPeriodo(Date dataInicioPeriodo) {
        this.dataInicioPeriodo = dataInicioPeriodo;
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estoque_dh_fim", insertable = true, updatable = true)
    public Date getDataFimPeriodo() {
        return dataFimPeriodo;
    }

    public void setDataFimPeriodo(Date dataFimPeriodo) {
        this.dataFimPeriodo = dataFimPeriodo;
    }

    @Id
    @Column(name = "mercado_cnpj", insertable = true, updatable = true)
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Id
    @Column(name = "produto_id",  insertable = true, updatable = true)
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @ManyToOne
    @NotNull
    @JoinColumn(name = "mercado_cnpj", referencedColumnName = "mercado_cnpj", insertable = false, updatable = false)
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @ManyToOne
    @NotNull
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

        Estoque estoque = (Estoque) o;

        if (!cnpj.equals(estoque.cnpj)) return false;
        if (!produtoId.equals(estoque.produtoId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cnpj.hashCode();
        result = 31 * result + produtoId.hashCode();
        return result;
    }
}
