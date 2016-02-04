package br.com.sicot.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.sicot.data.CompraLitProducer;
import br.com.sicot.model.Compra;
import br.com.sicot.model.Empresa;
import br.com.sicot.model.ItemCompra;
import br.com.sicot.model.ItemListaCompra;
import br.com.sicot.model.ListaCompra;
import br.com.sicot.model.Produto;
import br.com.sicot.service.CompraRegistration;
import br.com.sicot.util.ClienteCompra;
import br.com.sicot.vo.SubCompra;

/**
 * Created by thiago on 22/12/15.
 */
@Named
@Stateful
@ConversationScoped
public class CompraController implements Serializable {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Conversation conversation;

    @Inject
    private CompraRegistration compraRegistration;

    @Inject
    private CompraLitProducer compraLitProducer;

    @Inject
    private ClienteCompra clienteCompra;

    private List<Produto> produtosCompra;

    private List<SubCompra> subCompras;

    private Compra newCompra;

    private String filtroProduto;

    public void findProdutoOrderByNome() {

        if(filtroProduto==null || filtroProduto.isEmpty()){

            produtosCompra = compraLitProducer.findProdutoOrderByNome();

            return;
        }

        String fitros[] = filtroProduto.split(",");

        produtosCompra = compraLitProducer.findProdutoOrderByNome(Arrays.asList(fitros));

    }

    public void adicionar(Produto produto){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        BigDecimal quatidade = new BigDecimal(request.getParameter("produtos:"+(produtosCompra.indexOf(produto))+":quantidade"));

        produto.setQuantidade(quatidade);

        if(produto.getQuantidade().equals(BigDecimal.ZERO)){
            return;
        }

        ItemCompra itemCompra = new ItemCompra();
        itemCompra.setCompra(newCompra);
        itemCompra.setProduto(produto);
        itemCompra.setProdutoId(produto.getId());
        itemCompra.setQuantidade(produto.getQuantidade());

        if(!this.newCompra.getItemCompras().contains(itemCompra)) {
            this.newCompra.getItemCompras().add(itemCompra);
        }
    }

    public void remover(ItemCompra itemCompra){
        this.newCompra.getItemCompras().remove(itemCompra);
    }

    public String obterValoreListaCompra(ListaCompra listaCompra){
        initNewCompra();

        for (ItemListaCompra itemListaCompra : listaCompra.getItemsListaCompra()) {
            ItemCompra item = new ItemCompra();
            item.setQuantidade(itemListaCompra.getQuantidade());
            item.setProduto(itemListaCompra.getProduto());
            item.setProdutoId(itemListaCompra.getProdutoId());
            item.setCompra(newCompra);
            item.setCompraId(newCompra.getId());

            newCompra.getItemCompras().add(item);
        }

        compraRegistration.obterValores(this.newCompra);
        return "/public/compra/pedido.xhtml?faces-redirect=true";
    }


    public String obterValores(){
        compraRegistration.obterValores(this.newCompra);

        return "/public/compra/pedido.xhtml?faces-redirect=true";
    }

    public String processarCompra() throws Exception {
        subCompras = new ArrayList<SubCompra>();
        HashMap<Empresa, List<ItemCompra>> map = new HashMap<Empresa, List<ItemCompra>>();

        for (ItemCompra item : this.newCompra.getItemCompras()) {
            if(map.get(item.getEmpresa())==null){
                map.put(item.getEmpresa(), new ArrayList<ItemCompra>());
            }

            map.get(item.getEmpresa()).add(item);
        }

        for (Empresa empresa : map.keySet()) {
            SubCompra subCompra = new SubCompra();
            subCompra.setEmpresa(empresa);

            for(ItemCompra item : map.get(empresa)){
                subCompra.getItens().add(item);
            }

            subCompras.add(subCompra);
        }
        this.newCompra.setCliente(this.clienteCompra.getClienteCompra());
        compraRegistration.register(this.newCompra);

        this.newCompra = new Compra();

        return "/public/compra/finalizar.xhtml?faces-redirect=true";
    }

    @PostConstruct
    private void initNewCompra() {

        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }

        newCompra = clienteCompra.getClienteCompra().getCompra();
        newCompra.setSituacao("P");//Pendente
        newCompra.setIcEntregar("N");
        newCompra.setQuantidadeParcela(new Long(0));
        newCompra.setItemCompras(new ArrayList<ItemCompra>());
        produtosCompra = compraLitProducer.findProdutoDestaqueOrderByNome();
    }

    public String getFiltroProduto() {
        return filtroProduto;
    }

    public void setFiltroProduto(String filtroProduto) {
        this.filtroProduto = filtroProduto;
    }

    public List<SubCompra> getSubCompras() {
        return subCompras;
    }

    public void setSubCompras(List<SubCompra> subCompras) {
        this.subCompras = subCompras;
    }

    public List<Produto> getProdutosCompra() {
        return produtosCompra;
    }

    public void setProdutosCompra(List<Produto> produtosCompra) {
        this.produtosCompra = produtosCompra;
    }
}
