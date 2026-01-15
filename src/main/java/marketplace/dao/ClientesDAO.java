package marketplace.dao;

import jakarta.persistence.EntityManager;
import marketplace.model.Cliente;
import marketplace.model.Compra;
import marketplace.model.Vendedor;

import java.util.List;

public class ClientesDAO extends DAO<Cliente> {

    public ClientesDAO() {
        super(Cliente.class);
    }


    public List<Cliente> clientesBOT( ){
        String jpql  = "select c from Cliente c where c.bot = true and c.ativo = true";

        return em.createQuery(jpql, Cliente.class).getResultList();
    }
    public List<Cliente> clientesCompraAtivaBOT( ){
        String jpql  = "select c from Cliente c join c.compras ped where c.bot = true and c.ativo = true and ped.ativo = true";

        return em.createQuery(jpql, Cliente.class).getResultList();
    }

    public Cliente Pesquisar(String email, String senha){
        String jpql  = "select c from Cliente c where c.email = :email and c.senha = :senha";

        return em.createQuery(jpql, Cliente.class).setParameter("email", email).setParameter("senha", senha)
                .getSingleResult();
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
