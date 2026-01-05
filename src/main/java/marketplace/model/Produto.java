package marketplace.model;


import jakarta.persistence.*;


@Entity
public class Produto extends EntidadeBD{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_prod;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double valorUnitario;

    @Column(nullable = false)
    private int quantidade;

    @ManyToOne()
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;


    private int vendas = 0;


    public Produto() {
    }

    public Produto(String nome, Double valorUnitario, int quantidade) {

        this.nome = nome;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public boolean temEstoque(){
        return this.quantidade > 0;
    }


    public Long getId_prod() {
        return id_prod;
    }

    public void setId_prod(Long id_prod) {
        this.id_prod = id_prod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public int getVendas() {
        return vendas;
    }

    public void addVendas(int vendas) {
        this.vendas = getVendas()+vendas;
    }
}
