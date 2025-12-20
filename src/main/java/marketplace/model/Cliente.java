package marketplace.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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
        assert carrinhoAtual != null;
        carrinhoAtual.finalizarCompra();


    }

}
