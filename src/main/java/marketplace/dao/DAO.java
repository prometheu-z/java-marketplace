package marketplace.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DAO <E> {

    protected static final EntityManagerFactory emf;
    protected final EntityManager em;
    protected final Class<E> classe;

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

    @SafeVarargs
    public final DAO<E> persistir(E... objetos){
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


    //Crud

    public DAO<E> incluirAtomico(E entidade){
        if(em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }

        return this.iniciar().persistir(entidade).fechar();

    }



    public List<E> buscarID(int quantidade, int deslocamento){
        
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
