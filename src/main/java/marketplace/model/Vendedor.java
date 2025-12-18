package marketplace.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeLoja;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "id_prod")
    private List<Produto> estoque;

    @OneToMany(mappedBy = "id_venda")
    private List<Venda> vendas;


    public Vendedor() {
    }

    public Vendedor(List<Venda> vendas, List<Produto> estoque, String senha, String cnpj, String nomeLoja) {
        this.vendas = vendas; // vendas nulo, no set
        this.estoque = estoque; // estoque nulo, no set
        this.senha = senha;
        this.cnpj = cnpj;
        this.nomeLoja = nomeLoja;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Produto> getEstoque() {
        return estoque;
    }

    public void setEstoque(List<Produto> estoque) {
        this.estoque = estoque;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
}
