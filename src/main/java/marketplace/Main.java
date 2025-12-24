package marketplace;

import marketplace.dao.ClientesDAO;
import marketplace.dao.DAO;
import marketplace.model.*;

import java.util.List;

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
        c.finalizarCompra();
        c.comprarProduto(p3, 2);
        c.comprarProduto(p2, 3);

        c.finalizarCompra();


        DAO<Object> daoV = new DAO<>(Object.class);


        daoV.iniciar().persistir(v,v2,c).fechar();

        ClientesDAO dao = new ClientesDAO();

        List<Compra> compras = dao.getUltimasCompras(c);

        for (Compra compra : compras){
            System.out.println("----------------------------------");
            System.out.println(compra.getId_compra()+"      cliente: "+compra.getCliente().getNome()+"  valor total:"+compra.getValorTotal());
            System.out.println("itens        quant       valor");
            for (ItemCompra i : compra.getItens()){
                System.out.println(i.getProduto().getNome()+"      "+i.getQuantidade()+"      "+i.getSubTotal());
            }
            System.out.println("---------------------------------- ");

        }



    }
}
