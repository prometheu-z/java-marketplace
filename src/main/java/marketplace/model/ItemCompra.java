package marketplace.model;


import jakarta.persistence.*;


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


    public Long getId_itemVenda() {
        return id_itemVenda;
    }

    public void setId_itemVenda(Long id_itemVenda) {
        this.id_itemVenda = id_itemVenda;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(Double valorAtual) {
        this.valorAtual = valorAtual;
    }
}
