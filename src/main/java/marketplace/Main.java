package marketplace;

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



        venda.criarProduto(v.getId());
        venda.criarProduto(v.getId());
        venda.criarProduto(v.getId());
        venda.criarProduto(v2.getId());
        venda.criarProduto(v2.getId());

        venda.atualizarProduto(1L, 1L);


        //Gerar compras
        for (int i = 0; i<4; i++) {

            try {
                compra.adicionarProduto(c.getId(), 5L, 1);
                compra.finalizarCompra(c);


                compra.adicionarProduto(c.getId(), 3L, 2);
                compra.finalizarCompra(c);

                compra.adicionarProduto(c.getId(), 2L, 3);
                compra.finalizarCompra(c);

                compra.adicionarProduto(c.getId(), 1L, 4);
                compra.finalizarCompra(c);

                compra.adicionarProduto(c.getId(), 4L, 5);
                compra.finalizarCompra(c);

            } catch (CarrinhoNuloException | ProdutoInvalidoException e){
                System.out.println(e.getMessage());
            }

        }


        view.mostrarHistorico(c);

        vv.dashProdutos(v);



    }
}
