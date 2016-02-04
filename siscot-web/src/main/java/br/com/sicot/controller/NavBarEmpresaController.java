package br.com.sicot.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sicot.data.CompraLitProducer;
import br.com.sicot.model.Compra;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.model.Produto;
import br.com.sicot.util.EmpresaEstoque;
import br.com.sicot.vo.ProdutoPendente;

/**
 * Created by thiago on 29/12/15.
 */
@Model
public class NavBarEmpresaController {

	@Inject
    private CompraLitProducer compraLitProducer;

    @Inject
    private EmpresaEstoque empresaEstoque;

    private List<Compra> pendentesEmpresa;
    
    private ArrayList<ProdutoPendente> produtoPendentes;

    @Produces
    @Named
    public List<Compra> getPendentesEmpresa() {
    	
    	if(empresaEstoque.getEmpresaEstoque()!=null) {
    		pendentesEmpresa = compraLitProducer.findCompraPendenteOrderByData(empresaEstoque.getEmpresaEstoque());
        }
    	
		if (pendentesEmpresa != null) {

			HashMap<Produto, BigDecimal> produtos = new HashMap<Produto, BigDecimal>();

			for (Compra compra : pendentesEmpresa) {
				for (ItemCompra item : compra.getItemCompras()) {
					
					if(item.getEmpresa().equals(item.getEmpresa()) && item.getSituacao().equals("P")){
					
						BigDecimal quantidade = BigDecimal.ZERO;
						
						if (produtos.containsKey(item.getProduto())) {
							
							quantidade = produtos.get(item.getProduto()).add(item.getQuantidade());
							
							produtos.put(item.getProduto(), quantidade);
						} else {
							quantidade = item.getQuantidade();
							
							produtos.put(item.getProduto(), quantidade);
						}
						
					}
				}
			}

			produtoPendentes = new ArrayList<ProdutoPendente>();

			for (Produto produto : produtos.keySet()) {

				produtoPendentes.add(new ProdutoPendente(produto, produtos.get(produto)));

			}

		}
    	
        return pendentesEmpresa;
    }

    @Produces
    @Named
	public ArrayList<ProdutoPendente> getProdutoPendentes() {
		return produtoPendentes;
	}   
    
}
