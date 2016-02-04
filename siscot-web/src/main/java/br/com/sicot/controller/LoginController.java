package br.com.sicot.controller;

import br.com.sicot.model.Cliente;
import br.com.sicot.model.Empresa;
import br.com.sicot.model.User;
import br.com.sicot.service.ClienteRegistration;
import br.com.sicot.service.EmpresaRegistration;
import br.com.sicot.service.UserRegistration;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by thiago on 15/12/15.
 */
@ManagedBean
@ViewScoped
public class LoginController {
    public static final String PAGINA_INDEX = "/admin.xhtml";
    public static final String PAGINA_INDEX_2 = "/index.xhtml";
    public static final String PAGINA_INDEX_3 = "/empresa.xhtml?faces-redirect=true";
    public static final String PAGINA_LOGIN = "/login.xhtml?faces-redirect=true";
    public static final String PAGINA_ACESSO = "/acesso.xhtml?faces-redirect=true";

    private String usuario;
    private String senha;
    private Boolean remmenber;

    @Inject
    private UserRegistration userRegistration;

    @Inject
    private ClienteRegistration clienteRegistration;

    @Inject
    private EmpresaRegistration empresaRegistration;

    public String onClickLogar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            request.login(this.usuario, this.senha);

            if(remmenber){
                Cookie userCookie = new Cookie("vtusername", usuario);
                userCookie.setMaxAge(3600);
                ((HttpServletResponse) facesContext.getExternalContext()
                        .getResponse()).addCookie(userCookie);

                Cookie passCokie = new Cookie("vtpassword", senha);
                passCokie.setMaxAge(3600);
                ((HttpServletResponse) facesContext.getExternalContext()
                        .getResponse()).addCookie(passCokie);
            }

            final User user = userRegistration.findById(usuario);

            request.getSession().setAttribute("USUARIO", user);

            if(isAdmin()){
                return PAGINA_INDEX;
            }

            if(isCliente()) {
                Cliente cliente = clienteRegistration.findByEmail(user.getEmail());

                facesContext.getExternalContext().getSessionMap().put("clienteCompra", cliente);
                return PAGINA_INDEX_2;
            }

            if(isEmpresa()) {
                final Empresa empresa = empresaRegistration.findByEmail(user.getEmail());
                request.getSession().setAttribute("EMPRESA", empresa);
                return PAGINA_INDEX_3;
            }
            return PAGINA_INDEX_2;
        } catch (ServletException e) {
            //se houver erro no Login Module vai cair aqui...
            //Você pode fazer log por exemplo!
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //tratar aqui mensagens de segurança que possam ter vindo
            //do Login Module exibindo-as na forma de FacesMessage
        }

        return null;
    }

    public String onClickLogout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String urlRequest = request.getRequestURL().toString();

        facesContext.getExternalContext().invalidateSession();

        return urlRequest.contains("private") ? PAGINA_LOGIN : PAGINA_ACESSO;
    }

    public User getUser(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        return (User) request.getSession().getAttribute("USUARIO");
    }

    public boolean isAdmin(){
        if(getUser()==null){
            return false;
        }
       return userRegistration.isAdmin(getUser());
    }

    public boolean isEmpresa(){
        if(getUser()==null){
            return false;
        }
        return userRegistration.isEmpresa(getUser());
    }

    public boolean isCliente(){
        if(getUser()==null){
            return false;
        }
        return userRegistration.isCliente(getUser());
    }

    public Date getDataHora(){
        return new Date();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getRemmenber() {
        return remmenber;
    }

    public void setRemmenber(Boolean remmenber) {
        this.remmenber = remmenber;
    }
}
