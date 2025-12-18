package marketplace.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CollectionIdJdbcTypeCode;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_prod;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double valorUnitario;

    @Column(nullable = false)
    private int quantidade;

    public Produto() {
    }

    public Produto(Long id_prod, String nome, Double valorUnitario, int quantidade) {
        this.id_prod = id_prod;
        this.nome = nome;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
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
}
