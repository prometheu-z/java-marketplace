package marketplace.model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClienteTest {

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    static void config(){
        emf = Persistence.createEntityManagerFactory("sislog");

    }

    @BeforeEach
    void inicio(){
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



    @Test
    void AdicionarCliente(){

        Cliente cliente = new Cliente("Gabriel", "bielzin@gmail.com", "algo123");

        em.persist(cliente);
        em.getTransaction().commit();

        em.close();

        Cliente cBusca = em.find(Cliente.class, cliente.getId());

        assertNotNull(cBusca);
        assertEquals("Gabriel", cliente.getNome());

    }


}
