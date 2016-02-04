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
@Table(name = "unidade_medida")
@SequenceGenerator(name = "unidade_medida_seq", sequenceName = "unidade_medida_unidade_medida_id_seq", allocationSize = 1, initialValue = 1)
public class UnidadeMedida {
    private Long id;
    private String sigla;
    private String nome;

    @Id
    @Column(name = "unidade_medida_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =  "unidade_medida_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 5)
    @Column(name = "unidade_medida_sigla")
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @NotNull
    @Size(max = 20)
    @Column(name = "unidade_medida_nome")
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

        UnidadeMedida that = (UnidadeMedida) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
