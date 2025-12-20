package marketplace.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ItemCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_itemVenda;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Compra compra;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private Double valorAtual;

    public ItemCompra() {
    }

    public ItemCompra(Compra compra, Produto produto, int quantidade) {
        produto.getVendedor().adicionarVendas(this);
        this.compra = compra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorAtual = produto.getValorUnitario();
    }

    public Double getSubTotal(){
        return this.quantidade * this.valorAtual;


    }
}
