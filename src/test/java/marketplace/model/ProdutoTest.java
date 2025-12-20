package marketplace.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

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
    void adicionarProduto(){

        Produto produto = new Produto("Monitor", 1259.99, 2);

        em.persist(produto);
        em.getTransaction().commit();

        Produto pBusca = em.find(Produto.class, produto.getId_prod());

        assertNotNull(pBusca);


    }

    @Test
    void removerProduto(){

        Produto produto = new Produto("Monitor", 1259.99, 2);

        em.persist(produto);
        em.flush();

        em.clear();

        Produto pRemov = em.find(Produto.class, produto.getId_prod());
        em.remove(pRemov);
        em.getTransaction().commit();

        Produto pBusca = em.find(Produto.class, produto.getId_prod());

        assertNull(pBusca);

    }

    @Test
    void updateProduto(){

        Produto produto = new Produto("Monitor", 1259.99, 2);

        em.persist(produto);
        em.flush();

        em.clear();

        Produto pUpdate = em.find(Produto.class, produto.getId_prod());

        pUpdate.setNome("Placa de video");
        pUpdate.setValorUnitario(459.0);
        pUpdate.setQuantidade(6);
        em.getTransaction().commit();

        Produto pBusca = em.find(Produto.class, produto.getId_prod());

        assertNotNull(pBusca);
        assertEquals("Placa de video", pBusca.getNome());



    }
}

