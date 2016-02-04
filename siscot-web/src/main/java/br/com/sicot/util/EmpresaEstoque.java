package br.com.sicot.util;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.com.sicot.model.Empresa;

/**
 * Created by thiago on 05/01/16.
 */
@SessionScoped
public class EmpresaEstoque implements Serializable {

    @Produces
    @Named("empresaEstoque")
    public Empresa getEmpresaEstoque(){
        return (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EMPRESA");
    }

}
