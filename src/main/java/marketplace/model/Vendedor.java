package marketplace.model;

import jakarta.persistence.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "vendedor",cascade = CascadeType.ALL)
    private List<Produto> estoque = new ArrayList<>();

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<ItemCompra> itemvendas = new ArrayList<>();


    public Vendedor() {
    }

    public Vendedor(String nomeLoja, String senha, String cnpj) {
        this.senha = senha;
        this.cnpj = cnpj;
        this.nomeLoja = nomeLoja;
    }

    public void adicionarEstoque(Produto produto){
        this.estoque.add(produto);
        produto.setVendedor(this);
    }

    public void adicionarVendas(ItemCompra itemCompra){
        this.itemvendas.add(itemCompra);
        itemCompra.setVendedor(this);
    }

    public void removerEstoque(Produto produto){
        this.estoque.remove(produto);
    }

    public void removerVendas(ItemCompra itemCompra){
        this.itemvendas.remove(itemCompra);
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

    public List<ItemCompra> getItemvendas() {
        return itemvendas;
    }

    public void setItemvendas(List<ItemCompra> itemvendas) {
        this.itemvendas = itemvendas;
    }
}
