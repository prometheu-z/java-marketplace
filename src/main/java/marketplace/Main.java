package marketplace;

import marketplace.dao.DAO;
import marketplace.model.Cliente;
import marketplace.model.Produto;
import marketplace.model.Vendedor;

public class Main {
    public static void main(String[] args) {
        Cliente c = new Cliente("marcos", "marquin@gmail.com", "1223");
        Vendedor v = new Vendedor("lucas", "22334", "123432");
        Vendedor v2 = new Vendedor("vini", "33224", "123432");

        Produto p1 = new Produto("lapis", 1.20, 20);
        Produto p2 = new Produto("borracha", 2.50, 12);
        Produto p3 = new Produto("tesoura", 5.0, 8);
        Produto p4 = new Produto("cola", 12.0, 6);






        v.adicionarEstoque(p1);
        v.adicionarEstoque(p2);
        v.adicionarEstoque(p3);
        v2.adicionarEstoque(p4);
        v2.adicionarEstoque(p2);


        c.comprarProduto(p1, 1);
        c.comprarProduto(p3, 2);
        c.comprarProduto(p2, 3);

        c.finalizarCompra();


        DAO<Object> daoV = new DAO<>(Object.class);


        daoV.iniciar().persistir(v,v2,c).fechar();



    }
}
