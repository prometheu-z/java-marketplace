package marketplace.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.service.CompraService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CompraTest {

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
