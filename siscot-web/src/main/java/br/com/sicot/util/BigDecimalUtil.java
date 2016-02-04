package br.com.sicot.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalUtil {
	
	public static String converterEmValorMonetarioBrasileiroComSimboloMonetario(BigDecimal valor) {
		return valor == null ? null : NumberFormat.getCurrencyInstance().format(valor);
	}
	
	public static String converterEmValorMonetarioBrasileiro(BigDecimal valor) {
		Locale locale = new Locale("pt","BR");
		return valor == null ? null : NumberFormat.getCurrencyInstance(locale).format(valor).replace("R$ ", "");
	}
	
	public static String converterEmValorMonetarioBrasileiroSimples(BigDecimal valor) {
		//return valor == null ? null : NumberFormat.getCurrencyInstance().format(valor).replace("R$ ", "").replaceAll("\\.", "");
		Locale locale = new Locale("pt","BR");
		return valor == null ? null : NumberFormat.getCurrencyInstance(locale).format(valor).replace("R$ ", "");
	}

	public static BigDecimal converterValorMonetarioBrasileiroEmBigDecimal(String valorTransacao) {
		return valorTransacao == null ? null : new BigDecimal(valorTransacao.replace("R$ ", "").replaceAll("\\.", "").replace(",", "."));
	}
}
