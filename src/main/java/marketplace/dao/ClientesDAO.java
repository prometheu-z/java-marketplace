package marketplace.dao;

import marketplace.model.Cliente;
import marketplace.model.Compra;

import java.util.List;

public class ClientesDAO extends DAO<Cliente> {

    public List<Compra> getUltimasCompras(Cliente cliente){
        String jpql = "select c from Compra c where c.cliente = :cliente order by c.horario desc";

        return em.createQuery(jpql, Compra.class).setParameter("cliente", cliente).
                setMaxResults(10).getResultList();

    }


}
g