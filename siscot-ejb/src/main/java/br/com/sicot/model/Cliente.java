package br.com.sicot.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@Table(name = "cliente")
public class Cliente implements Serializable{
    private String cpf;
    private String nome;
    private String sexo;
    private Date dataNascimento;
    private String email;
    private String telefoneComercial;
    private String telefoneResidencial;
    private String celular;
    private String situacao;
    private String cep;
    private short cidade;
    private String endereco;
    private String password;
    private Compra compra;

    @Id
    @Column(name = "cliente_cpf")
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Column(name = "cliente_nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "cliente_sexo")
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "cliente_data_nascimento")
    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Column(name = "cliente_email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "cliente_telefone_comercial")
    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    @Column(name = "cliente_telefone_residencial")
    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    @Column(name = "cliente_celular")
    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Column(name = "cliente_situacao")
    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Column(name = "cliente_cep")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "cliente_cidade")
    public short getCidade() {
        return cidade;
    }

    public void setCidade(short cidade) {
        this.cidade = cidade;
    }

    @Column(name = "cliente_endereco" )
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Transient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public Compra getCompra() {
        if(compra==null){
            compra = new Compra();
        }
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @Transient
    public int getQtdItens(){
        if(getCompra().getItemCompras()!=null){
            return getCompra().getItemCompras().size();
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        if (cpf != null ? !cpf.equals(cliente.cpf) : cliente.cpf != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cpf != null ? cpf.hashCode() : 0;
    }
}
