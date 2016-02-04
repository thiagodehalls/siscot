package br.com.sicot.util;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("br.com.sicot.util.ConverterBigDecimalBrasileiroSimples")
public class ConverterBigDecimalBrasileiroSimples implements Converter{

	public Object getAsObject(FacesContext facesContext, UIComponent component, String string) throws ConverterException {
		try {
			if(string != null && !string.trim().equals("")) {
				return BigDecimalUtil.converterValorMonetarioBrasileiroEmBigDecimal(string); 
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valor inválido", null));
		}
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object objeto) throws ConverterException {
		return objeto == null ? null : BigDecimalUtil.converterEmValorMonetarioBrasileiroSimples(new BigDecimal(objeto.toString()));
	}

}
