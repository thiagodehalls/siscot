package br.com.sicot.controller;

import br.com.sicot.data.CompraLitProducer;
import br.com.sicot.model.Compra;
import br.com.sicot.util.ClienteCompra;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by thiago on 29/12/15.
 */
@Model
public class NavBarController {

    @Inject
    private CompraLitProducer compraLitProducer;

    @Inject
    private ClienteCompra clienteCompra;

    private List<Compra> comprasPendentes;

    @Produces
    @Named
    public List<Compra> getComprasPendentes() {
        if(clienteCompra.getClienteCompra()!=null) {
            comprasPendentes = compraLitProducer.findCompraPendenteOrderByData(clienteCompra.getClienteCompra());
        }
        return comprasPendentes;
    }

}
