package marketplace.dao;

import jakarta.persistence.EntityManager;
import marketplace.model.Produto;
import marketplace.model.Vendedor;

import java.util.List;

public class VendedorDAO extends DAO<Vendedor> {

    public VendedorDAO() {
        super(Vendedor.class);
    }

    public List<Produto> itensDoEstoque(Vendedor vendedor){
        String jpql = "select p from produto p where p.vendedor = :vendedor";

        return em.createQuery(jpql, Produto.class).
                setParameter("vendedor", vendedor.getId()).
                getResultList();
    }





}
