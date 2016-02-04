package br.com.sicot.controller;

import br.com.sicot.data.EstoqueListProducer;
import br.com.sicot.model.Empresa;
import br.com.sicot.model.Estoque;
import br.com.sicot.model.Produto;
import br.com.sicot.model.RelacaoProduto;
import br.com.sicot.service.EstoqueRegistration;
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
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thiago on 14/12/15.
 */
@Named
@Stateful
@ConversationScoped
public class EstoqueController implements Serializable {
    @Inject
    private FacesContext facesContext;

    @Inject
    private Conversation conversation;

    @Inject
    private EstoqueListProducer estoqueListProducer;

    @Inject
    private EstoqueRegistration estoqueRegistration;

    private List<Estoque> estoques;
    private List<Produto> produtosEstoques;
    private Empresa empresa;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }

        if (this.id != null) {
            try {
                initNewEstoque();
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    @PostConstruct
    private void initNewEstoque() {
        if(this.id!=null){
           produtosEstoques = estoqueListProducer.findProdutoOrderByNome(this.id);
           estoques = estoqueListProducer.findEstoqueOrderByProduto(this.id);
        }

        if(produtosEstoques!=null) {
           for (Produto produto : produtosEstoques) {
               Estoque estoque = new Estoque();

               estoque.setProduto(produto);
               estoque.setProdutoId(produto.getId());
               estoque.setDataInicioPeriodo(new Date());
               estoque.setDataFimPeriodo(new Date());
               estoque.setCnpj(this.getId());
               estoque.setQuantidade(new BigDecimal(0));
               estoque.setValorUnitario(new BigDecimal(0));
               estoque.setEmpresa(empresa);

               estoques.add(estoque);
           }
       }
    }

    public void register() throws  Exception {
        this.conversation.end();
        try{

            for (Estoque estoque : estoques) {

                estoqueRegistration.register(estoque);
            }

            estoques.clear();
            produtosEstoques.clear();

            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);

            initNewEstoque();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
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

    @Produces
    @Named
    public List<Estoque> getEstoques() {
        return estoques;
    }

    @Produces
    @Named
    public List<Produto> getProdutosEstoques() {
        return produtosEstoques;
    }
}
