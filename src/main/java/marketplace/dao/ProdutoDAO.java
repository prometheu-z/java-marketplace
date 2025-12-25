package marketplace.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import marketplace.model.Produto;

import java.util.List;

public class ProdutoDAO extends DAO<Produto> {

    public ProdutoDAO() {
        super(Produto.class);
    }

    //TODO sobrecarga pra direrentes filtros
    public List<Produto> listarProdutos(int inicio, int quantidade){

        String jpql = "select p from produto p";
        return em.createQuery(jpql, Produto.class).getResultList();

    }

    public Produto buscaProId(Long id){
        return em.find(Produto.class, id);
    }
}

