package marketplace.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import marketplace.dao.*;
import marketplace.model.Produto;
import marketplace.model.Vendedor;
import marketplace.view.VendedorView;

public class VendasService {
    private EntityManager em;
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

    private void abreTransacao() {
        this.em = DAO.criarEM();

        this.em.getTransaction().begin();

        daoP.setEntity(this.em);
        daoV.setEntity(this.em);
        daoC.setEntity(this.em);
        daoK.setEntity(this.em);

    }

    private void fechaTransacao(){

        try{
            if(em.getTransaction().isActive()){
                em.getTransaction().commit();
            }
        } finally {
            if(em.isOpen()){
                em.close();
            }
        }

    }

    private void desfazerTransacao(){
        if(em != null && em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }
        if(em != null && em.isOpen()){
            em.close();
        }

    }
    public void criarProduto(Long idVendedor, String nomeProd, Double preco, int qtd){

        abreTransacao();

        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                //TODO add exception
                return;
            }

            if(qtd <= 0 || preco < 0){
                //TODO add exception
                return;
            }

            Produto produto = new Produto(nomeProd, preco, qtd);
            vendedor.adicionarEstoque(produto);
            daoP.persistir(produto);

            daoP.merge(produto);

            fechaTransacao();
        } catch (Exception e) {
            desfazerTransacao();
            //TODO exception
        }
    }
    public void excluirProduto(Long idVendedor, Long idProduto){
        abreTransacao();

        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            Produto produto = daoP.buscarPorId(idProduto);
            if(vendedor == null || produto == null){
                //TODO add exception
                return;
            }
            if(!produto.getVendedor().getId().equals(vendedor.getId())){
                //todo exception
                return;
            }

            vendedor.removerEstoque(produto);

            daoP.remover(produto);

            fechaTransacao();

        } catch (Exception e) {
            desfazerTransacao();
            //todo exception

        }


    }

    public void alterarProduto(Long idVendedor, Long idProduto){
        abreTransacao();
        VendedorView view = new VendedorView();

        //todo criar logica pra decidir se deve criar um novo produto pra mudanças graves ou manter para mudanças minimas de preço e nome

        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            Produto produto = daoP.buscarPorId(idProduto);
            if(vendedor == null || produto == null){
                //TODO add exception
                return;
            }
            if(!produto.getVendedor().getId().equals(vendedor.getId())){
                //todo exception
                return;
            }
            Produto produtoAlterado = view.alterarProduto(produto);
            if(produtoAlterado == null){
                System.out.println("Operação cancelada");
                desfazerTransacao();
                return;
            }

            produto.setNome(produtoAlterado.getNome());
            produto.setValorUnitario(produtoAlterado.getValorUnitario());
            produto.setQuantidade(produtoAlterado.getQuantidade());

            daoP.merge(produto);

            fechaTransacao();

        } catch (Exception e) {
            desfazerTransacao();
            //todo exception

        }

    }


}
