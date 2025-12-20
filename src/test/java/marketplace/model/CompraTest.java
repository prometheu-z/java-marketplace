package marketplace.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class VendaTest {

    static private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    static void inicio(){
        emf = Persistence.createEntityManagerFactory("sislog");


    }

    @BeforeEach
    void abrir(){
        em = emf.createEntityManager();

        em.getTransaction().begin();
    }

    @AfterEach
    void fechar(){
        if(em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }

        em.close();
    }

    @AfterAll
    static void terminar(){
        emf.close();
    }



}
