package marketplace.service;

import jakarta.persistence.EntityManager;
import marketplace.dao.*;
import marketplace.exceptions.CarrinhoNuloException;
import marketplace.exceptions.ClienteInvalidoException;
import marketplace.exceptions.OperacaoCompraException;
import marketplace.exceptions.ProdutoInvalidoException;
import marketplace.model.*;

public class CompraService {
    private EntityManager em;

    private ClientesDAO daoC;
    private CompraDAO daoK;
    private ProdutoDAO daoP;

    public CompraService() {
        this.daoP = new ProdutoDAO();
        this.daoK = new CompraDAO();
        this.daoC = new ClientesDAO();
    }

    private void abreTransacao() {
        this.em = DAO.criarEM();

        this.em.getTransaction().begin();

        daoC.setEntity(this.em);
        daoK.setEntity(this.em);
        daoP.setEntity(this.em);
    }

    private void fechaTransacao() {
        try {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
    private void desfazerTransacao(){
        if(em != null && em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }
        if(em!= null && em.isOpen()){
            em.close();
        }
    }



    public void adicionarProduto(Long idCliente, Long idProduto, int quantidade) {
        abreTransacao();



        try {

            Cliente cliente = daoC.buscarPorId(idCliente);
            if(cliente == null){
                throw new ClienteInvalidoException("Cliente de código: "+ idCliente+" não encontrado");
            }

            Produto produto = daoP.buscarPorId(idProduto);
            if(produto == null || !produto.isAtivo()){
                throw new ProdutoInvalidoException("Produto de código: "+idProduto+" não encontrado ou inativo");
            }

            Compra carrinho = daoC.compraAtiva(cliente);
            if(carrinho == null){
                carrinho = new Compra(cliente);
                daoK.persistir(carrinho);
            }

            carrinho.adicionarItem(produto, quantidade);
            produto.setQuantidade(produto.getQuantidade()-quantidade);

            daoK.merge(carrinho);
            daoP.merge(produto);

            fechaTransacao();
        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e) {
            desfazerTransacao();

            throw new OperacaoCompraException("Não foi possível adicionar o produto ao carrinho", e);
        }
    }


    public void finalizarCompra(Cliente cliente){
        abreTransacao();


        try {
            Compra carrinho = daoC.compraAtiva(cliente);
            if(carrinho == null){
                throw new CarrinhoNuloException("O cliente não possui itens no carrinho");
            }

            for(ItemCompra item : carrinho.getItens()){
                item.getProduto().addVendas(item.getQuantidade());
            }

            carrinho.finalizarCompra();
            daoK.merge(carrinho);
            fechaTransacao();
        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e) {
            desfazerTransacao();

            throw new OperacaoCompraException("Não foi possível finalizar sua compra", e);
        }
    }

    public void removerProduto(Long idCliente, Long idProduto){

        abreTransacao();

        try {
            Cliente cliente = daoC.buscarPorId(idCliente);

            if(cliente == null){
                throw new ClienteInvalidoException("Cliente de código: "+ idCliente+" não encontrado");
            }

            Produto produto = daoP.buscarPorId(idProduto);
            if(produto == null || !produto.isAtivo()){
                throw new ProdutoInvalidoException("Produto de código: "+idProduto+" não encontrado");
            }

            Compra carrinho = daoC.compraAtiva(cliente);
            if(carrinho == null){
                throw new CarrinhoNuloException("O cliente não possui itens no carrinho");
            }

            ItemCompra itemRemover = daoK.itemPeloProduto(carrinho, idProduto);
            if(itemRemover == null){
                throw new ProdutoInvalidoException("O produto "+produto.getNome()+" não está no seu carrinho");
            }



            Produto prod = itemRemover.getProduto();
            prod.setQuantidade(prod.getQuantidade()+itemRemover.getQuantidade());
            daoP.merge(prod);

            carrinho.removerItem(itemRemover);

            daoK.merge(carrinho);

            fechaTransacao();

        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch(Exception e){
            desfazerTransacao();

            throw new OperacaoCompraException("Não foi possível remover o item do carrinho", e);

        }



    }

}
