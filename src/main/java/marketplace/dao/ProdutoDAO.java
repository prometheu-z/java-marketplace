package marketplace.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import marketplace.model.Produto;

import java.util.List;

public class ProdutoDAO extends DAO<Produto> {

    public ProdutoDAO() {
        super(Produto.class);
    }

    public Long numProdutos(){
        String jpql = "select Count(p) from Produto p where p.ativo = true and p.quantidade > 0";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    public Long numProdutosValMin(Double minPreco){
        String jpql = "select Count(p) from Produto p where p.ativo = true and p.quantidade > 0 and p.valorUnitario < :minPreco";

        return em.createQuery(jpql, Long.class).setParameter("minPreco", minPreco).getSingleResult();

    }
    public Long numProdutosVendas(int vendas){
        String jpql = "select Count(p) from Produto p where p.ativo = true and p.quantidade > 0 and p.vendas > :vendas";

        return em.createQuery(jpql, Long.class)
                .setParameter("vendas", vendas).getSingleResult();

    }



    // — -≥ basico
    public List<Produto> listarProdutos(int inicio, int quantidade){

        String jpql = "select p from Produto p where p.ativo = true and p.quantidade > 0";
        return em.createQuery(jpql, Produto.class).setFirstResult(inicio).setMaxResults(quantidade).getResultList();

    }
    // — -≥ ordem alfabética
    public List<Produto> listarProdutosAlph(int inicio, int quantidade){
        String jpql = "select p from Produto p where p.ativo = true and p.quantidade > 0  order by p.nome";
        return em.createQuery(jpql, Produto.class).setFirstResult(inicio).setMaxResults(quantidade).getResultList();
    }
    // — -≥ preço mínimo
    public List<Produto> listarProdutosValMin(int inicio, int quantidade, Double minPreco){

        String jpql = "select p from Produto p where p.ativo = true and p.quantidade > 0 and p.valorUnitario < :minPreco";
        return em.createQuery(jpql, Produto.class).setParameter("minPreco", minPreco).setFirstResult(inicio).
                setMaxResults(quantidade).getResultList();

    }
    //— -≥ vendas
    public List<Produto> listarProdutosVendas(int inicio, int quantidade, int vendas){

        String jpql = "select p from Produto p where p.ativo = true and p.quantidade > 0 and p.vendas > :vendas order by p.vendas desc";

        return em.createQuery(jpql, Produto.class)
                .setParameter("vendas", vendas)
                .setFirstResult(inicio)
                .setMaxResults(quantidade)
                .getResultList();

    }

    public Produto buscaProId(Long id){
        return em.find(Produto.class, id);
    }
}

