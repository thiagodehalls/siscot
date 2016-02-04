package br.com.sicot.controller;

import br.com.sicot.data.RelacaoProdutoListProducer;
import br.com.sicot.model.*;
import br.com.sicot.service.ProdutoRegistration;
import br.com.sicot.service.RelacaoProdutoRegistration;
import br.com.sicot.util.Constantes;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiago on 11/12/15.
 */
@Named
@Stateful
@ConversationScoped
public class RelacaoProdutoController implements Serializable {

    @Inject
    private Conversation conversation;

    @Inject
    private FacesContext facesContext;

    @Inject
    private RelacaoProdutoRegistration relacaoProdutoRegistration;

    @Inject
    private ProdutoRegistration produtoRegistration;

    @Inject
    private RelacaoProdutoListProducer relacaoProdutoListProducer;

    private Produto produto;

    private List<Produto> selecionados;
    private List<RelacaoProduto> excluidos;
    private List<RelacaoProduto> relacaoProdutos;

    private List<Produto> relacionados;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Produces
    @Named
    public List<RelacaoProduto> getRelacaoProdutos() {
        return relacaoProdutos;
    }

    @Named
    @Produces
    public List<Produto> getRelacionados() {
        return relacionados;
    }

    @Produces
    @Named
    public Produto getProduto() {
        return produto;
    }

    @Produces
    @Named
    public List<Produto> getSelecionados() {
        return selecionados;
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }

        if (this.id == null) {
            initNewRelacaoProduto();
        } else {
            try {
                this.produto = produtoRegistration.findById(getId());
                relacaoProdutoListProducer.setFilterRelacaoProduto(this.id);

                relacionados = relacaoProdutoListProducer.findProdutoOrderByNome();
                relacaoProdutos = relacaoProdutoListProducer.findRelacaoProduto();
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    public void register() throws  Exception {
        this.conversation.end();
        try{

            for (Produto relacionado : selecionados) {

                RelacaoProduto relacaoProduto =
                        new RelacaoProduto(produto.getId(), relacionado.getId());

                relacaoProdutoRegistration.register(relacaoProduto);
            }

            for (RelacaoProduto excluido : excluidos){

                relacaoProdutoRegistration.remove(excluido);
            }

            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);

            initNewRelacaoProduto();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    private void initNewRelacaoProduto() {
        produto = new Produto();
        produto.setCategoria(new Categoria());
        produto.setUnidadeMedida(new UnidadeMedida());
        produto.setMarcaFabricante(new MarcaFabricante());
        selecionados = new ArrayList<Produto>();
        relacaoProdutos = new ArrayList<RelacaoProduto>();
        relacionados = new ArrayList<Produto>();
        excluidos = new ArrayList<RelacaoProduto>();
    }

    public void addRelacao(Produto produtoRelacao){
        RelacaoProduto relacaoProduto = new RelacaoProduto();

        relacaoProduto.setProduto(this.produto);
        relacaoProduto.setProdutoRelacao(produtoRelacao);
        relacaoProduto.setProdutoId(this.produto.getId());
        relacaoProduto.setProdutoIdRelacao(produtoRelacao.getId());

        relacaoProdutos.add(relacaoProduto);
        selecionados.add(produtoRelacao);

        relacionados.remove(produtoRelacao);
    }

    public void removeRelacao(RelacaoProduto relacaoProduto){
        relacaoProdutos.remove(relacaoProduto);
        selecionados.remove(relacaoProduto.getProdutoRelacao());

        relacionados.add(relacaoProduto.getProdutoRelacao());

        excluidos.add(relacaoProduto);
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
}
