package marketplace;

import marketplace.dao.ClientesDAO;
import marketplace.dao.DAO;
import marketplace.model.*;
import marketplace.service.CompraService;
import marketplace.service.VendasService;
import marketplace.view.ClienteView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Cliente c = new Cliente("marcos", "marquin@gmail.com", "1223");
        Vendedor v = new Vendedor("Materiais escolares do lu", "22334", "123432");
        Vendedor v2 = new Vendedor("Fabercastel", "33224", "123432");


        DAO<Object> daoV = new DAO<>(Object.class);
        CompraService compra = new CompraService();
        VendasService venda = new VendasService();
        ClienteView view = new ClienteView();

        daoV.iniciar().persistir(v,v2,c).fechar();


        venda.criarProduto(v.getId(), "lapis", 1.20, 20);
        venda.criarProduto(v.getId(), "borracha", 2.50, 12);
        venda.criarProduto(v.getId(), "tesoura", 5.0, 8);
        venda.criarProduto(v2.getId(), "cola", 12.0, 6);
        venda.criarProduto(v2.getId(), "tesoura", 5.0, 8);



        compra.adicionarProduto(c.getId(), 1L, 1);
        compra.adicionarProduto(c.getId(), 3L, 2);
        compra.adicionarProduto(c.getId(), 2L, 3);
        compra.adicionarProduto(c.getId(), 1L, 4);
        compra.adicionarProduto(c.getId(), 4L, 5);
        compra.removerProduto(c.getId(), 3L);

        compra.finalizarCompra(c);

        view.gerarNotaFiscal(c);

//        List<Compra> compras = dao.getUltimasCompras(c, 10);
//
//        for (Compra compra : compras){
//            System.out.println("----------------------------------");
//            System.out.println(compra.getId_compra()+"      cliente: "+compra.getCliente().getNome()+"  valor total:"+compra.getValorTotal());
//            System.out.println("itens        quant       valor");
//            for (ItemCompra i : compra.getItens()){
//                System.out.println(i.getProduto().getNome()+"      "+i.getQuantidade()+"      "+i.getSubTotal());
//            }
//
//        }



    }
}
