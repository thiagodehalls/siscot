package br.com.sicot.vo;

import java.math.BigDecimal;

import br.com.sicot.model.Produto;

public class ProdutoPendente{
	private Produto produto;
	private BigDecimal quantidade;

	public ProdutoPendente(Produto produto, BigDecimal quantidade) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
	}

	public ProdutoPendente() {
		super();
	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
}
