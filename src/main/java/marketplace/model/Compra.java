package marketplace.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;


@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_compra;

    //TO  DO: ver as implicancias do cascate
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    private LocalDateTime horario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<ItemCompra> itens = new ArrayList<>();


    @Column(nullable = false)
    private Double valorTotal = 0.0;

    @Column(nullable = false)
    private Boolean compraAtiva;




    public Compra(Cliente cliente) {
        this.cliente = cliente;

        this.compraAtiva = true;
    }


    public void adicionarItem(Produto produto, Integer quantidade) {

        ItemCompra item = new ItemCompra(this, produto, quantidade);

        this.itens.add(item);
        this.valorTotal += item.getSubTotal();
    }
    public void removerItem(ItemCompra item) {

        this.itens.remove(item);
        this.valorTotal -= item.getSubTotal();
    }

    // adicionar exceçaso de venda vazia
    public void finalizarCompra(){
        this.horario = LocalDateTime.now();
        this.compraAtiva = false;

    }


    public Long getId_compra() {
        return id_compra;
    }

    public void setId_compra(Long id_compra) {
        this.id_compra = id_compra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public List<ItemCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemCompra> itens) {
        this.itens = itens;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getCompraAtiva() {
        return compraAtiva;
    }

    public void setCompraAtiva(Boolean compraAtiva) {
        this.compraAtiva = compraAtiva;
    }
}
