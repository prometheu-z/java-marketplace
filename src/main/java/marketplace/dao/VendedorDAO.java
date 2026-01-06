package marketplace.dao;

import marketplace.model.Produto;
import marketplace.model.Vendedor;

import java.util.List;

public class VendedorDAO extends DAO<Vendedor> {

    public VendedorDAO() {
        super(Vendedor.class);
    }
    public Long numProdutosVendedor(Vendedor vendedor){
        String jpql = "select Count(p) from Produto p where p.vendedor.id = :vendedor and p.ativo = true";

        return em.createQuery(jpql, Long.class).setParameter("vendedor", vendedor.getId()).getSingleResult();
    }

    public List<Produto> itensDoEstoque(Vendedor vendedor, int inicio, int quantidade){
        String jpql = "select p from Produto p where p.vendedor.id = :vendedor and p.ativo = true order by p.vendas desc";

        return em.createQuery(jpql, Produto.class).
                setParameter("vendedor", vendedor.getId()).
                setFirstResult(inicio).
                setMaxResults(quantidade).
                getResultList();
    }





}
