package marketplace.service;

import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.dao.ProdutoDAO;
import marketplace.dao.VendedorDAO;
import marketplace.model.Produto;
import marketplace.model.Vendedor;

public class VendasService {
    private ClientesDAO daoC;
    private CompraDAO daoK;
    private VendedorDAO daoV;
    private ProdutoDAO daoP;

    public VendasService() {
        this.daoC = new ClientesDAO();
        this.daoK = new CompraDAO();
        this.daoV = new VendedorDAO();
        this.daoP = new ProdutoDAO();
    }

    public void criarProduto(Long idVendedor, String nomeProd, Double preco, int qtd){

        Vendedor vendedor = daoV.buscarPorId(idVendedor);
        if(vendedor == null){
            //TODO add exception
            return;
        }

        Produto produto = new Produto(nomeProd, preco, qtd);
        vendedor.adicionarEstoque(produto);

        daoP.iniciar().persistir(produto).fechar();


    }


}
