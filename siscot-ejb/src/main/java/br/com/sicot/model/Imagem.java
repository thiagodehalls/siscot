package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@Table(name = "imagem")
@SequenceGenerator(name = "image_seq", sequenceName = "imagem_imagem_id_seq", allocationSize = 1, initialValue = 1)
public class Imagem {
    private Long id;
    private String caminho;
    private String titulo;
    private Long ordem;
    private Produto produto;

    public Imagem() {
        this.ordem = 0L;
    }

    @Id
    @Column(name = "imagem_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 200)
    @Column(name = "imagem_caminho")
    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    @NotNull
    @Size(max = 30)
    @Column(name = "imagem_titulo")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @NotNull
    @Column(name = "imagem_ordem")
    public Long getOrdem() {
        return ordem;
    }

    public void setOrdem(Long ordem) {
        this.ordem = ordem;
    }

    @ManyToOne
    @NotNull
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id")
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

        Imagem imagem = (Imagem) o;

        if (!id.equals(imagem.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
