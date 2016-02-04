package br.com.sicot.vo;

import br.com.sicot.model.Cliente;
import br.com.sicot.model.Empresa;
import br.com.sicot.model.ItemCompra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiago on 28/12/15.
 */
public class SubCompra {
	private Empresa empresa;
	private Cliente cliente;
	private List<ItemCompra> itens = new ArrayList<ItemCompra>();

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompra> itens) {
		this.itens = itens;
	}
}
