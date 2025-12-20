import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import marketplace.model.Cliente;
import marketplace.model.Produto;
import marketplace.model.Vendedor;

public class MainTest {
    public static void main(String[] args) {
        Cliente c = new Cliente("Gab", "gabb@", "32212");
        Vendedor v = new Vendedor("gaga", "345433", "123432");
        Vendedor v2 = new Vendedor("jojo", "345433", "123432");

        Produto p1 = new Produto("geladeira", 1200.0, 2);
        Produto p2 = new Produto("monitor", 850.0, 1);
        Produto p3 = new Produto("mouse", 120.0, 4);
        Produto p4 = new Produto("pente", 12.0, 6);




        v.adicionarEstoque(p1);
        v.adicionarEstoque(p2);
        v.adicionarEstoque(p3);
        v2.adicionarEstoque(p4);
        v2.adicionarEstoque(p2);


        c.comprarProduto(p1, 1);
        c.comprarProduto(p3, 2);
        c.comprarProduto(p2, 3);

        c.finalizarCompra();


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sislog");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(c);
        em.persist(v);



        em.getTransaction().commit();


        em.close();


    }
}
