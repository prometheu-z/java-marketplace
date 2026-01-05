package marketplace.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import marketplace.model.Produto;

import java.util.List;

public class ProdutoDAO extends DAO<Produto> {

    public ProdutoDAO() {
        super(Produto.class);
    }

    // — -≥ basico
    public List<Produto> listarProdutos(int inicio, int quantidade){

        String jpql = "select p from produto p where p.ativo = true and p.quantidade > 0";
        return em.createQuery(jpql, Produto.class).setFirstResult(inicio).setMaxResults(quantidade).getResultList();

    }
    // — -≥ ordem alfabética
    public List<Produto> listarProdutos(int inicio, int quantidade, String asc){
        String jpql = "select p from produto p where p.ativo = true and p.quantidade > 0 order by p.nome";
        return em.createQuery(jpql, Produto.class).setFirstResult(inicio).setMaxResults(quantidade).getResultList();

    }
    // — -≥ preço mínimo
    public List<Produto> listarProdutos(int inicio, int quantidade, Double minPreco){
        String jpql = "select p from produto p where p.ativo = true and p.valorUnitario < :minPreco and p.quantidade > 0";
        return em.createQuery(jpql, Produto.class).setParameter("minPreco", minPreco).setFirstResult(inicio).setMaxResults(quantidade).getResultList();

    }
    //— -≥ vendas
    public List<Produto> listarProdutos(int inicio, int quantidade, int vendas){
        String jpql = "select p from produto p where p.ativo = true and p.vendas > :vendas and p.quantidade > 0 order by p.vendas desc";
        return em.createQuery(jpql, Produto.class).setParameter("vendas", vendas).setFirstResult(inicio).setMaxResults(quantidade).getResultList();

    }

    public Produto buscaProId(Long id){
        return em.find(Produto.class, id);
    }
}

