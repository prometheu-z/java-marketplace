package marketplace.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DAO <E> {

    protected static final EntityManagerFactory emf;
    protected  EntityManager em;
    protected  Class<E> classe;

    static {
        //TO DO try catch
        emf = Persistence.createEntityManagerFactory("sislog");
    }




    public DAO(Class<E> classe){
        this.classe = classe;
        this.em = emf.createEntityManager();

    }
    public void setEntity(EntityManager em) {
        this.em = em;
    }

    public static EntityManager criarEM(){
        return emf.createEntityManager();
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
    public DAO<E> merge(E entidade){
        em.merge(entidade);
        return this;
    }

    public void rollback(){
        if(em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }
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



    public E buscarPorId(Long id){

        return em.find(classe, id);


    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
