package marketplace.model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
    void adicionarCliente(){

        Cliente cliente = new Cliente("Gabriel", "bielzin@gmail.com", "algo123");

        em.persist(cliente);
        em.getTransaction().commit();

        Cliente cBusca = em.find(Cliente.class, cliente.getId());

        assertNotNull(cBusca);


    }

    @Test
    void removerCliente(){

        Cliente cliente = new Cliente("Gabriel", "bielzin@gmail.com", "algo123");

        em.persist(cliente);
        em.flush();

        em.clear();

        Cliente cRemov = em.find(Cliente.class, cliente.getId());
        em.remove(cRemov);
        em.getTransaction().commit();

        Cliente cBusca = em.find(Cliente.class, cliente.getId());

        assertNull(cBusca);

    }

    @Test
    void updateCliente(){
        Cliente cliente = new Cliente("Gabriel", "bielzin@gmail.com", "algo123");

        em.persist(cliente);
        em.flush();

        em.clear();

        Cliente cUpdate = em.find(Cliente.class, cliente.getId());

        cUpdate.setNome("Atlas");
        cUpdate.setEmail("Atlas@gmail.com");
        em.getTransaction().commit();

        Cliente cBusca = em.find(Cliente.class, cliente.getId());

        assertNotNull(cBusca);
        assertEquals("Atlas", cBusca.getNome());



    }




}
