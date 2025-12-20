package marketplace.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.ResultSet;
import java.util.List;

public class DAO <E> {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private Class<E> classe;

    static {
        //TO DO try catch
        emf = Persistence.createEntityManagerFactory("sislog");
    }


    public DAO(){
        this(null);

    }
    public DAO(Class<E> classe){
        this.classe = classe;
        em = emf.createEntityManager();

    }

    public DAO<E> iniciar(){
        em.getTransaction().begin();

        return this;
    }
    public DAO<E> persistir(E... objetos){
        for(E obj : objetos){
            em.persist(obj);
        }
        return this;
    }
    public DAO<E> fechar(){
        em.getTransaction().commit();
        return this;
    }

    public void finalizar(){
        em.close();
    }

    public List<E> buscarI(int quantidade, int deslocamento){
        
        if(classe == null){
            throw new UnsupportedOperationException("Classe nula");
        }

        String jpql = "select e from "+ classe.getName()+" e";
        TypedQuery<E> result = em.createQuery(jpql,classe);

        return result
                .setFirstResult(quantidade)
                .setMaxResults(deslocamento)
                .getResultList();
    }

}
