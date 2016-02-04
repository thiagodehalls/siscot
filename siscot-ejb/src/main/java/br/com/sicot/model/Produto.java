package br.com.sicot.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by thiago on 20/11/15.
 */
@Entity
@XmlRootElement
@Table(name = "produto")
@SequenceGenerator(name = "produto_seq", sequenceName = "produto_produto_id_seq", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable{
    private Long id;
    private String nome;
    private BigDecimal medida;
    private String descricao;
    private String palavrasChave;
    private String destaque;
    private Collection<Atributo> atributos;
    private Collection<Estoque> estoques;
    private Collection<Imagem> imagems;
    private Categoria categoria;
    private MarcaFabricante marcaFabricante;
    private UnidadeMedida unidadeMedida;
    private Collection<RelacaoProduto> relacaoProdutos;
    private BigDecimal precoMedio;
    private BigDecimal quantidade = new BigDecimal(1);
    private Boolean selecinado = Boolean.FALSE;

    @Id
    @Column(name = "produto_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 50)
    @Column(name = "produto_nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @NotNull
    @Column(name = "produto_medida")
    public BigDecimal getMedida() {
        return medida;
    }

    public void setMedida(BigDecimal medida) {
        this.medida = medida;
    }

    @NotNull
    @Size(max = 200)
    @Column(name = "produto_descricao")
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @NotNull
    @Size(max = 500)
    @Column(name = "palavras_chave")
    public String getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    @NotNull
    @Column(name = "produto_destaque")
    public String getDestaque() {
        return destaque;
    }

    public void setDestaque(String destaque) {
        this.destaque = destaque;
    }

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    public Collection<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(Collection<Atributo> atributos) {
        this.atributos = atributos;
    }

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    public Collection<Estoque> getEstoques() {
        return estoques;
    }

    public void setEstoques(Collection<Estoque> estoques) {
        this.estoques = estoques;
    }

    @OneToMany(mappedBy = "produto", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    public Collection<Imagem> getImagems() {
        return imagems;
    }

    public void setImagems(Collection<Imagem> imagems) {
        this.imagems = imagems;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id")
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "marca_fabricante_id", referencedColumnName = "marca_fabricante_id")
    public MarcaFabricante getMarcaFabricante() {
        return marcaFabricante;
    }

    public void setMarcaFabricante(MarcaFabricante marcaFabricante) {
        this.marcaFabricante = marcaFabricante;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unidade_medida_id", referencedColumnName = "unidade_medida_id")
    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    @OneToMany(mappedBy = "produto")
    public Collection<RelacaoProduto> getRelacaoProdutos() {
        return relacaoProdutos;
    }

    public void setRelacaoProdutos(Collection<RelacaoProduto> relacaoProdutos) {
        this.relacaoProdutos = relacaoProdutos;
    }

    @Column(name = "produto_preco_medio")
    public BigDecimal getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(BigDecimal precoMedio) {
        this.precoMedio = precoMedio;
    }

    @Transient
    public BigDecimal getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    @Transient
    public Boolean getSelecinado() {
        return selecinado;
    }

    public void setSelecinado(Boolean selecinado) {
        this.selecinado = selecinado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Produto produto = (Produto) o;

        if (!id.equals(produto.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
