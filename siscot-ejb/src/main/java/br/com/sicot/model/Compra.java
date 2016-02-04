package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Collection;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@Table(name = "compra")
@SequenceGenerator(name = "compra_seq", sequenceName = "compra_compra_id_seq", allocationSize = 1, initialValue = 1)
public class Compra implements Serializable{

    private Long id;
    private Date dataHora;
    private String situacao;
    private String icEntregar;
    private Long quantidadeParcela;
    private Cliente cliente;
    private Collection<ItemCompra> itemCompras;

    BigDecimal valorTotalCompra;
    BigDecimal valorTotalDesconto;

    private Date dataInicio;
    private Date dataFim;

    @Id
    @Column(name = "compra_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =  "compra_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "compra_data_hora")
    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    @NotNull
    @Size(max = 1)
    @Column(name = "compra_situacao")
    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @NotNull
    @Size(max = 1)
    @Column(name = "compra_entregar")
    public String getIcEntregar() {
        return icEntregar;
    }

    public void setIcEntregar(String icEntregar) {
        this.icEntregar = icEntregar;
    }

    @NotNull
    @Column(name = "compra_quantidade_parcela")
    public Long getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public void setQuantidadeParcela(Long quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
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

    @OneToMany(mappedBy = "compra", fetch = FetchType.LAZY)
    public Collection<ItemCompra> getItemCompras() {
        return itemCompras;
    }

    public void setItemCompras(Collection<ItemCompra> itemCompras) {
        this.itemCompras = itemCompras;
    }

    @Transient
    public BigDecimal getValorTotalCompra() {
        this.valorTotalCompra = new BigDecimal(0);
        if(getItemCompras()!=null) {
            for (ItemCompra item : getItemCompras()) {
                this.valorTotalCompra = this.valorTotalCompra.add(item.getValor()!=null ? item.getValor() : new BigDecimal(0));
            }
        }
        return valorTotalCompra;
    }

    public void setValorTotalCompra(BigDecimal valorTotalCompra) {
        this.valorTotalCompra = valorTotalCompra;
    }

    @Transient
    public BigDecimal getValorTotalDesconto() {
        this.valorTotalDesconto = new BigDecimal(0);
        if(getItemCompras()!=null) {
            for (ItemCompra item : getItemCompras()) {
                this.valorTotalDesconto = this.valorTotalDesconto.add(item.getValorObtidoDesconto());
            }
        }
        return valorTotalDesconto;
    }

    public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
        this.valorTotalDesconto = valorTotalDesconto;
    }

    @Transient
    public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Transient
    public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Compra compra = (Compra) o;

        if (id != null ? !id.equals(compra.id) : compra.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
