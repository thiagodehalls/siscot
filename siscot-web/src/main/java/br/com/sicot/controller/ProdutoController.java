package br.com.sicot.controller;

import br.com.sicot.model.*;
import br.com.sicot.service.ProdutoRegistration;
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
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by thiago on 04/12/15.
 */
@Named
@Stateful
@ConversationScoped
public class ProdutoController implements Serializable {
    @Inject
    private FacesContext facesContext;

    @Inject
    private ProdutoRegistration produtoRegistration;

    @Inject
    private Conversation conversation;

    private Produto newProduto;

    private Atributo newAtributo;

    private Imagem newImagem;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

        if (this.id == null) {
            initNewProduto();
        } else {
            try {
                this.newProduto = produtoRegistration.findById(getId());
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
    }

    @Produces
    @Named
    public Produto getNewProduto() {
        return newProduto;
    }

    @Produces
    @Named
    public Atributo getNewAtributo(){
        return newAtributo;
    }

    @Produces
    @Named
    public Imagem getNewImagem() {
        return newImagem;
    }

    public void register() throws  Exception {
        this.conversation.end();
        try{
            produtoRegistration.register(newProduto);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewProduto();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
    }

    public void registerAtributo() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String newAtributoNome = request.getParameter("newAtributoNome");
        String newAtributoTexto = request.getParameter("newAtributoTexto");
        String newAtributoOrdem = request.getParameter("newAtributoOrdem");

        validarParametrosAtrituto(newAtributoNome, newAtributoTexto, newAtributoOrdem);

        if(!FacesMessage.FACES_MESSAGES.isEmpty()){
            return;
        }

        newAtributo.setNome(newAtributoNome);
        newAtributo.setTexto(newAtributoTexto);
        newAtributo.setOrdem(Long.parseLong(newAtributoOrdem));
        newAtributo.setProduto(newProduto);

        newProduto.getAtributos().add(newAtributo);

        newAtributo = new Atributo();
    }

    private void validarParametrosAtrituto(String newAtributoNome, String newAtributoTexto, String newAtributoOrdem) {
        if(newAtributoNome.isEmpty()){
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Nome: Erro de validação: o valor é necessário");
            facesContext.addMessage(null, m);
        }
        if(newAtributoTexto.isEmpty()){
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Valor: Erro de validação: o valor é necessário");
            facesContext.addMessage(null, m);
        }

        if(newAtributoOrdem.isEmpty()){
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Ordem de Exibição: Erro de validação: o valor é necessário");
            facesContext.addMessage(null, m);
        }
        else{
            try{
                Long.parseLong(newAtributoOrdem);
            }catch (Exception e){
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Ordem de Exibição: Erro de validação: o valor não é válido");
                facesContext.addMessage(null, m);
            }
        }
    }

    public void registerImagem() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String newImagemTitulo = request.getParameter("newImagemTitulo");
        String newImagemOrdem = request.getParameter("newImagemOrdem");
        String newImagemCaminho = request.getParameter("newImagemCaminho");

        validarParametrosImagem(newImagemCaminho, newImagemOrdem, newImagemOrdem);

        if(!FacesMessage.FACES_MESSAGES.isEmpty()){
            return;
        }

        newImagem.setCaminho(newImagemCaminho);
        newImagem.setTitulo(newImagemTitulo);
        newImagem.setOrdem(Long.parseLong(newImagemOrdem));
        newImagem.setProduto(newProduto);

        newProduto.getImagems().add(newImagem);

        newImagem = new Imagem();
    }

    private void validarParametrosImagem(String newImagemCaminho, String newImagemOrdem, String newImagemTitulo) {

        if(newImagemCaminho.isEmpty()){
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Caminho: Erro de validação: o valor é necessário");
            facesContext.addMessage(null, m);
        }
        if(newImagemTitulo.isEmpty()){
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Título: Erro de validação: o valor é necessário");
            facesContext.addMessage(null, m);
        }
        if(newImagemOrdem.isEmpty()){
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Ordem de Exibição: Erro de validação: o valor é necessário");
            facesContext.addMessage(null, m);
        }
        else{
            try{
                Long.parseLong(newImagemOrdem);
            }catch (Exception e){
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Ordem de Exibição: Erro de validação: o valor não é válido");
                facesContext.addMessage(null, m);
            }
        }
    }

    public void remove() throws  Exception {
        try{
            produtoRegistration.remove(newProduto);
            this.conversation.end();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewProduto();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao excluir.");
            facesContext.addMessage(null, m);
        }
    }

    public void removeAtributo(Atributo atributo) {

        newProduto.getAtributos().remove(atributo);

        newAtributo = new Atributo();
    }

    public void removeImagem(Imagem imagem) {

        newProduto.getImagems().remove(imagem);

        newImagem = new Imagem();
    }

    @PostConstruct
    private void initNewProduto() {
        newProduto = new Produto();
        newProduto.setCategoria(new Categoria());
        newProduto.setUnidadeMedida(new UnidadeMedida());
        newProduto.setMarcaFabricante(new MarcaFabricante());
        newProduto.setAtributos(new ArrayList<Atributo>());
        newProduto.setDestaque("N");
        newAtributo=new Atributo();
        newImagem=new Imagem();
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
