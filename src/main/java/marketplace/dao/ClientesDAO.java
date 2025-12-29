package marketplace.dao;

import jakarta.persistence.EntityManager;
import marketplace.model.Cliente;
import marketplace.model.Compra;

import java.util.List;

public class ClientesDAO extends DAO<Cliente> {

    public ClientesDAO() {
        super(Cliente.class);
    }


    public List<Compra> getUltimasCompras(Cliente cliente,int inicio, int quantidade){
        String jpql = "select c from Compra c where c.cliente = :cliente order by c.horario desc";

        return em.createQuery(jpql, Compra.class).setParameter("cliente", cliente).setFirstResult(inicio).
                setMaxResults(quantidade).getResultList();

    }
    public Long numCompras(Cliente cliente){
        String jpql = "select Count(c) from Compra c where c.cliente.id= :cliente";
        return em.createQuery(jpql, Long.class).setParameter("cliente", cliente.getId()).getSingleResult();

    }
    public Compra compraAtiva(Cliente cliente){
        String jpql = "select c from Compra c left join fetch c.itens where c.cliente = :cliente and c.compraAtiva = true";

        List<Compra> result = em.createQuery(jpql, Compra.class).setParameter("cliente", cliente).
                getResultList();

        if(result.isEmpty()){
            return null;
        }
        return result.getFirst();
    }







}
