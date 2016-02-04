package br.com.sicot.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@Table(name = "empresa")
public class Empresa implements Serializable{
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String telefone;
    private String email;
    private String telefone2;
    private Long cidade;
    private String cep;
    private String endereco;
    private String situacao;
    private String password;

    @Id
    @Column(name = "mercado_cnpj")
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Column(name = "empresa_razao_social")
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    @Column(name = "empresa_nome_fantasia")
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    @Column(name = "empresa_telefone")
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Email
    @Column(name = "empresa_email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "empresa_telefone_2")
    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    @Column(name = "empresa_cidade")
    public Long getCidade() {
        return cidade;
    }

    public void setCidade(Long cidade) {
        this.cidade = cidade;
    }

    @Column(name = "empresa_cep")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "empresa_endereco")
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Column(name = "empresa_situacao")
    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Transient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Empresa empresa = (Empresa) o;

        if (cnpj != null ? !cnpj.equals(empresa.cnpj) : empresa.cnpj != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cnpj != null ? cnpj.hashCode() : 0;
    }
}
