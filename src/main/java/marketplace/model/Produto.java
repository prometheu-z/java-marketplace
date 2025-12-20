package marketplace.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;


    public Produto() {
    }

    public Produto(String nome, Double valorUnitario, int quantidade) {
        this.nome = nome;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }



}
