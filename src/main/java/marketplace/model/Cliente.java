package marketplace.model;


import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;


@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Compra> compras = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public void comprarProduto(Produto  produto, int quantidade){

        Compra carrinhoAtual = null;

        // TO DO: substituir por um jpql
        for (Compra c : this.compras) {
            if (c.getCompraAtiva()) {
                carrinhoAtual = c;
                break;
            }
        }

        if (carrinhoAtual == null) {
            carrinhoAtual = new Compra(this);
            this.compras.add(carrinhoAtual);
        }
        carrinhoAtual.adicionarItem(produto, quantidade);
    }

    public void finalizarCompra(){
        Compra carrinhoAtual = null;

        // TO DO: substituir por um jpql
        for (Compra c : this.compras) {
            if (c.getCompraAtiva()) {
                carrinhoAtual = c;
                break;
            }
        }
        if(carrinhoAtual == null) {
            //add exception
        }
        carrinhoAtual.finalizarCompra();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
