package marketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
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

}
