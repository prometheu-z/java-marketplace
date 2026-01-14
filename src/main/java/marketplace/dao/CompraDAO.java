package marketplace.dao;

import jakarta.persistence.NoResultException;
import marketplace.model.Compra;
import marketplace.model.ItemCompra;
import marketplace.model.Produto;
import marketplace.model.Vendedor;

import java.util.List;

public class CompraDAO extends DAO<Compra>{
    public CompraDAO() {
        super(Compra.class);
    }

    public ItemCompra itemPeloProduto(Compra compra, Long idProduto){
        String jpql = "select i from ItemCompra i where i.compra = :compra and i.produto.id_prod = :produto";

        try {
            return em.createQuery(jpql, ItemCompra.class).setParameter("compra", compra)
                    .setParameter("produto", idProduto).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ItemCompra> getVendasDeProduto(Long produto, int inicio, int quantidade){
        String jpql = "select i from ItemCompra i where i.produto.id = :produto order by i.compra.horario desc";

        return em.createQuery(jpql, ItemCompra.class).setParameter("produto", produto).setFirstResult(inicio).
                setMaxResults(quantidade).getResultList();
    }
    public List<ItemCompra> getVendas(Vendedor vendedor, int inicio, int quantidade){
        String jpql = "select i from ItemCompra i where i.vendedor.id = :vendedor order by i.vendedor.id";

        return em.createQuery(jpql, ItemCompra.class).setParameter("vendedor", vendedor.getId()).setFirstResult(inicio).
                setMaxResults(quantidade).getResultList();
    }

    public Long numItensProdutos(Long produto){
        String jpql = "select Count(i) from ItemCompra i where i.produto.id = :produto";

        return em.createQuery(jpql, Long.class).setParameter("produto", produto).getSingleResult();
    }


    public Long totalVendas(Produto produto){
        String jpql = "select coalesce(sum(i.valorAtual), 0) from ItemCompra i where i.produto = :produto";

        return em.createQuery(jpql, Long.class).setParameter("produto", produto).getSingleResult();

    }

    public Long totalVendasDoVendedor(Long idVendedor){
        String jpql = "select coalesce(sum(i.quantidade), 0) from ItemCompra i where i.produto.vendedor.id = :id";

        return em.createQuery(jpql, Long.class).setParameter("id", idVendedor).getSingleResult();
    }

}
