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
@Table(name = "marca_fabricante")
@SequenceGenerator(name = "marca_fabricante_seq", sequenceName = "marca_fabricante_marca_fabricante_id_seq", allocationSize = 1, initialValue = 1)
public class MarcaFabricante {
    private Long id;
    private String nome;

    @Id
    @Column(name = "marca_fabricante_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marca_fabricante_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 30)
    @Column(name = "marca_fabricant_nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarcaFabricante that = (MarcaFabricante) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
