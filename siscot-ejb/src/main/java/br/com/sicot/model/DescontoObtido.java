package br.com.sicot.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Transient;

@NamedNativeQueries(value={
		@NamedNativeQuery(name=DescontoObtido.SUM_SIX_DESCONTO, resultClass=DescontoObtido.class,
							query="SELECT "
								 +"	distinct to_char(compra.compra_data_hora, 'YYYYMM') as mesAno, "
								 +"	(sum(item_compra.media_valores_acima * item_compra.item_compra_quantidade) "
								 +" - sum(item_compra.item_compra_valor_unitario * item_compra.item_compra_quantidade)) as totalDesconto "
								 +"FROM "
								 +"	cotacao.compra compra " 
								 +"INNER JOIN "
								 +"	cotacao.item_compra item_compra " 
								 +"		ON item_compra.compra_id = compra.compra_id "
								 +"WHERE "
								 +" cliente_cpf = ?"
								 +"GROUP BY "
								 +"	to_char(compra.compra_data_hora, 'YYYYMM') "
								 +"ORDER BY 1 "
								 +"LIMIT 12"),
		@NamedNativeQuery(name=DescontoObtido.SUM_DESCONTO, resultClass=DescontoObtido.class,
							query="SELECT "
								 +"	distinct to_char(now(), 'YYYYMM') as mesAno, "
								 +"	(sum(item_compra.media_valores_acima * item_compra.item_compra_quantidade) "
								 +" - sum(item_compra.item_compra_valor_unitario * item_compra.item_compra_quantidade)) as totalDesconto "
								 +"FROM "
								 +"	cotacao.compra compra " 
								 +"INNER JOIN "
								 +"	cotacao.item_compra item_compra " 
								 +"		ON item_compra.compra_id = compra.compra_id "
								 +"WHERE "
								 +"  cliente_cpf = ?" ),
		
		@NamedNativeQuery(name=DescontoObtido.SUM_COMPRA, resultClass=DescontoObtido.class,
							query="SELECT "
								 +"	distinct to_char(now(), 'YYYYMM') as mesAno, "
								 +"	sum(item_compra.item_compra_valor_unitario * item_compra.item_compra_quantidade) as totalDesconto "
								 +"FROM "
								 +"	cotacao.compra compra " 
								 +"INNER JOIN "
								 +"	cotacao.item_compra item_compra " 
								 +"		ON item_compra.compra_id = compra.compra_id "
								 +"WHERE "
								 +"  cliente_cpf = ?")
})
@Entity
public class DescontoObtido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String SUM_SIX_DESCONTO = "SUM_SIX_DESCONTO";
	public static final String SUM_DESCONTO = "SUM_DESCONTO";
	public static final String SUM_COMPRA = "SUM_COMPRA";
	
	@Id
	private String mesAno;
	private BigDecimal totalDesconto;
	
	@Transient
	private String mes;

	public String getMesAno() {
		return mesAno;
	}
	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
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
	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
}