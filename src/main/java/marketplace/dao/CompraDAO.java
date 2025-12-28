package marketplace.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import marketplace.model.Compra;
import marketplace.model.ItemCompra;

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
}
