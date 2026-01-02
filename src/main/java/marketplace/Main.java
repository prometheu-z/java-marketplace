package marketplace;

import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.dao.DAO;
import marketplace.dao.ProdutoDAO;
import marketplace.exceptions.CarrinhoNuloException;
import marketplace.exceptions.ProdutoInvalidoException;
import marketplace.model.*;
import marketplace.service.ClienteService;
import marketplace.service.VendedorService;
import marketplace.view.ClienteView;
import marketplace.view.VendedorView;

public class Main {
    public static void main(String[] args) {




        ClienteService compra = new ClienteService();
        VendedorService venda = new VendedorService();
        ClienteView view = new ClienteView();
        VendedorView vv = new VendedorView();

        Cliente c = compra.criarCliente();
        Vendedor v = venda.criarVendedor();
        Vendedor v2 = venda.criarVendedor();



        venda.criarProduto(v.getId(), "lapis", 1.20, 20);
        venda.criarProduto(v.getId(), "borracha", 2.50, 12);
        venda.criarProduto(v.getId(), "tesoura", 5.0, 8);
        venda.criarProduto(v2.getId(), "cola", 12.0, 6);
        venda.criarProduto(v2.getId(), "tesoura", 5.0, 8);

        venda.alterarProduto(1L, 1L);


        //Gerar compras
        for (int i = 0; i<4; i++) {

            try {
                compra.adicionarProduto(c.getId(), 9L, 1);
                compra.finalizarCompra(c);
            } catch (CarrinhoNuloException | ProdutoInvalidoException e){
                System.out.println(e.getMessage());
            }

            compra.adicionarProduto(c.getId(), 3L, 2);
            compra.finalizarCompra(c);

            compra.adicionarProduto(c.getId(), 2L, 3);
            compra.finalizarCompra(c);

            compra.adicionarProduto(c.getId(), 1L, 4);
            compra.finalizarCompra(c);

            compra.adicionarProduto(c.getId(), 4L, 5);
            compra.finalizarCompra(c);

        }


        view.mostrarHistorico(c);

        vv.dashProdutos(v);



    }
}
