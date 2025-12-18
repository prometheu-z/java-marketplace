package marketplace.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_venda;

    //TO  DO: ver as implicancias do cascate
    @ManyToOne()
    private Cliente comprador;

    @Column(nullable = false)
    private Date horario;

    @OneToMany(mappedBy = "id_prod")
    private List<Produto> itens;

    @Column(nullable = false)
    private Double valorTotal;

    @ManyToOne()
    private Vendedor vendedor;


    public Venda() {
    }

    public Venda(Vendedor vendedor, Double valorTotal, List<Produto> itens, Date horario, Cliente comprador) {
        this.vendedor = vendedor;
        this.valorTotal = valorTotal;
        this.itens = itens;
        this.horario = horario;
        this.comprador = comprador;
    }

    public Long getId_venda() {
        return id_venda;
    }

    public void setId_venda(Long id_venda) {
        this.id_venda = id_venda;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public List<Produto> getItens() {
        return itens;
    }

    public void setItens(List<Produto> itens) {
        this.itens = itens;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}
