package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@Table(name = "categoria")
@SequenceGenerator(name = "categoria_seq", sequenceName = "categoria_categoria_id_seq", allocationSize = 1, initialValue = 1)
public class Categoria implements Serializable{
    private Long id;
    private String nome;
    private List<Produto> produtos;

    public Categoria() {
    }

    @Id
    @Column(name = "categoria_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =  "categoria_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 30)
    @Column(name = "categoria_nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Categoria categoria = (Categoria) o;

        if (id != categoria.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
