package br.com.sicot.util;/**
 * Created by thiago on 23/12/15.
 */

import br.com.sicot.model.Cliente;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
public class ClienteCompra implements Serializable {

    @Produces
    @Named("clienteCompra")
    public Cliente getClienteCompra(){
        return (Cliente) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clienteCompra");
    }

}
