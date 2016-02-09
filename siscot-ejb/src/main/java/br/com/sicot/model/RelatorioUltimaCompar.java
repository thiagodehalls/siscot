package br.com.sicot.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Transient;

@NamedNativeQueries(value={
		@NamedNativeQuery(name=RelatorioUltimaCompar.DIFF_TWELVE_MONTH, resultClass=RelatorioUltimaCompar.class,
							query="SELECT "
								 +"	distinct to_char(compra.compra_data_hora, 'YYYYMM') as mesAno, "
								 +"	sum(item_compra.item_compra_valor_unitario * item_compra.item_compra_quantidade) as totalPagoMes, "
								 +"	sum(item_compra.media_valores_acima * item_compra.item_compra_quantidade) as totalPrecoMedio "
								 +"FROM "
								 +"	cotacao.compra compra " 
								 +"INNER JOIN "
								 +"	cotacao.item_compra item_compra " 
								 +"		ON item_compra.compra_id = compra.compra_id " 
								 +" WHERE "
								 +"  cliente_cpf = ?"
								 +"GROUP BY "
								 +"	to_char(compra.compra_data_hora, 'YYYYMM') "
								 +"ORDER BY 1 "
								 +"LIMIT 12")
})
@Entity
public class RelatorioUltimaCompar implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String DIFF_TWELVE_MONTH = "DIFF_TWELVE_MONTH";
	
	@Id
	private String mesAno;
	private BigDecimal totalPagoMes;
	private BigDecimal totalPrecoMedio;
	
	@Transient
	private String mes;
	
	public RelatorioUltimaCompar() {
		super();
	}

	public RelatorioUltimaCompar(String mesAno, BigDecimal totalPagoMes, BigDecimal totalPrecoMedio) {
		super();
		this.mesAno = mesAno;
		this.totalPagoMes = totalPagoMes;
		this.totalPrecoMedio = totalPrecoMedio;
	}
	
	public String getMesAno() {
		return mesAno;
	}
	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}
	public BigDecimal getTotalPagoMes() {
		return totalPagoMes;
	}
	public void setTotalPagoMes(BigDecimal totalPagoMes) {
		this.totalPagoMes = totalPagoMes;
	}
	public BigDecimal getTotalPrecoMedio() {
		return totalPrecoMedio;
	}
	public void setTotalPrecoMedio(BigDecimal totalPrecoMedio) {
		this.totalPrecoMedio = totalPrecoMedio;
	}

	public String getMes() {
		mes = mesAno.substring(0, 4)+"-";
		mes = mes + mesAno.substring(4, 6)+"-";
		mes = mes+"01";
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}
}
