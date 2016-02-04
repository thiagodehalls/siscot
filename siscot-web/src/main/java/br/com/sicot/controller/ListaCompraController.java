package br.com.sicot.controller;

import br.com.sicot.data.ListaCompraListProducer;
import br.com.sicot.model.Categoria;
import br.com.sicot.model.ItemListaCompra;
import br.com.sicot.model.ListaCompra;
import br.com.sicot.model.Produto;
import br.com.sicot.service.CompraRegistration;
import br.com.sicot.service.ListaCompraRegistration;
import br.com.sicot.util.ClienteCompra;
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

@Named
@Stateful
@ConversationScoped
public class ListaCompraController implements Serializable{

   @Inject
   private FacesContext facesContext;

   @Inject
   private Conversation conversation;

   @Inject
   private ListaCompraRegistration listaCompraRegistration;

   @Inject
   private ListaCompraListProducer listaCompraListProducer;

   @Inject
   private ClienteCompra clienteCompra;

   private ListaCompra newListaCompra;

   private List<ListaCompra> listaCompras;

   private List<Categoria> categoriasEager;

   private Long id;

   public Long getId() {
        return this.id;
   }

   public void setId(Long id) {
        this.id = id;
   }

   @Produces
   @Named
   public ListaCompra getNewListaCompra() {
      return newListaCompra;
   }

   public List<ListaCompra> getListaCompras() {
        if(clienteCompra.getClienteCompra()!=null){
            listaCompras= listaCompraListProducer.allListaCompraByClienteOrderedByNome(clienteCompra.getClienteCompra());
        }
        return listaCompras;
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
            initNewListaCompra();
        } else {
            try {
                newListaCompra = listaCompraRegistration.findById(getId());

                if(newListaCompra.getItemsListaCompra()!=null) {
                    getCategoriasEager();

                    for (ItemListaCompra itemListaCompra : newListaCompra.getItemsListaCompra()) {
                        for (Categoria categoria : categoriasEager) {
                           int index = categoria.getProdutos().indexOf(itemListaCompra.getProduto());
                           if(index>-1) {
                               categoria.getProdutos().get(index).setSelecinado(true);
                               categoria.getProdutos().get(index).setQuantidade(itemListaCompra.getProduto().getQuantidade());
                           }
                        }
                    }

                }
            } catch (Exception e) {
                String errorMessage = getRootErrorMessage(e);
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha na consulta por id.");
                facesContext.addMessage(null, m);
            }
        }
   }


   public void register() throws Exception {
       try {
           if(newListaCompra.getItemsListaCompra()==null) {
               newListaCompra.setItemsListaCompra(new ArrayList<ItemListaCompra>());
           }

           for (Categoria categoria : categoriasEager) {
               for (Produto produto : categoria.getProdutos()){

                   if (produto.getSelecinado()){
                       ItemListaCompra itemListaCompra = new ItemListaCompra();
                       itemListaCompra.setProduto(produto);
                       itemListaCompra.setProdutoId(produto.getId());
                       itemListaCompra.setListaCompra(newListaCompra);
                       itemListaCompra.setListaCompraId(newListaCompra.getId());
                       itemListaCompra.setQuantidade(produto.getQuantidade());

                       if(!newListaCompra.getItemsListaCompra().contains(itemListaCompra)){
                           newListaCompra.getItemsListaCompra().add(itemListaCompra);
                       }
                   }

               }
           }

           if(newListaCompra.getItemsListaCompra().isEmpty()){
               FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione um ou mais produtos para compor lista."
                       , "Selecione um ou mais produtos para compor lista.");
               facesContext.addMessage(null, m);
               return;
           }

           listaCompraRegistration.register(newListaCompra);
           FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
           facesContext.addMessage(null, m);
           initNewListaCompra();
       } catch (Exception e) {
           String errorMessage = getRootErrorMessage(e);
           FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
           facesContext.addMessage(null, m);
       }
   }

    public void remove() throws  Exception {
        try{
            listaCompraRegistration.remove(newListaCompra);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);
            initNewListaCompra();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao excluir.");
            facesContext.addMessage(null, m);
        }
    }

   @PostConstruct
   public void initNewListaCompra() {
      newListaCompra = new ListaCompra();
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

    public List<Categoria> getCategoriasEager() {
        if(categoriasEager==null){
            categoriasEager = listaCompraListProducer.allCategoriaEagerOrderedByNome();
        }
        return categoriasEager;
    }

    public void setCategoriasEager(List<Categoria> categoriasEager) {
        this.categoriasEager = categoriasEager;
    }
}
