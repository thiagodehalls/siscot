package br.com.sicot.model;

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
@Table(name = "atributo")
@SequenceGenerator(name = "atributo_seq", sequenceName = "atributo_atributo_id_seq", allocationSize = 1, initialValue = 1)
public class Atributo implements Serializable {
    private Long id;
    private String nome;
    private String texto;
    private Produto produto;
    private Long ordem;

    public Atributo() {
        this.ordem = 0L;
    }

    @Id
    @Column(name = "atributo_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "atributo_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 20)
    @Column(name = "atributo_nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @NotNull
    @Size(max = 100)
    @Column(name = "atributo_texto")
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @NotNull
    @Column(name = "atributo_ordem")
    public Long getOrdem() {
        return ordem;
    }

    public void setOrdem(Long ordem) {
        this.ordem = ordem;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id", insertable = true, updatable = true)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Atributo atributo = (Atributo) o;

        if (id != null ? !id.equals(atributo.id) : atributo.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
