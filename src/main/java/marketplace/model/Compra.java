package marketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_venda;

    //TO  DO: ver as implicancias do cascate
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;

    @Column(nullable = false)
    private LocalDateTime horario;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.PERSIST)
    private List<ItemVenda> itens = new ArrayList<>();

    @Column(nullable = false)
    private Double valorTotal = 0.0;




    public Venda() {
        this.horario = LocalDateTime.now();
    }


    public void adicionarItem(Produto produto, Integer quantidade) {

        ItemVenda item = new ItemVenda(this, produto, quantidade);

        this.itens.add(item);
        this.valorTotal += item.getSubTotal();
    }

}
