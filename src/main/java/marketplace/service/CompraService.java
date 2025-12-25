package marketplace.service;

import jakarta.persistence.EntityManager;
import marketplace.dao.*;
import marketplace.model.*;

import java.util.Objects;

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
                throw new IllegalArgumentException("Cliente n encontrado");
            }

            Produto produto = daoP.buscarPorId(idProduto);
            if(produto == null){
                throw new IllegalArgumentException("produto n encontrado");
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
        } catch (Exception e) {
            desfazerTransacao();
            //TODO re-lança exception
        }
    }

    public void finalizarCompra(Cliente cliente){
        abreTransacao();

        Compra carrinho = daoC.compraAtiva(cliente);

        if(carrinho == null){
            //TODO add exception
            return;
        }

        try {
            carrinho.finalizarCompra();
            daoK.merge(carrinho);
            fechaTransacao();
        } catch (Exception e) {
            desfazerTransacao();
        }
    }

    public void removerProduto(Long idCliente, Long idProduto){

        abreTransacao();

        Cliente cliente = daoC.buscarPorId(idCliente);
        Produto produto = daoP.buscarPorId(idProduto);
        Compra carrinho = daoC.compraAtiva(cliente);

        if(cliente == null){
            throw new IllegalArgumentException("Cliente n encontrado");
        }

        if(produto == null){
            throw new IllegalArgumentException("produto n encontrado");
        }

        if(carrinho == null){
            //TODO add exception
            return;
        }
        ItemCompra itemRemoveer = carrinho.getItens().stream().
                filter(i-> Objects.equals(i.getProduto().getId_prod(), produto.getId_prod())).
                findFirst().orElse(null);
        if(itemRemoveer == null){
            //TODO add exception
            return;
        }


        try {
            Produto prod = itemRemoveer.getProduto();
            prod.setQuantidade(prod.getQuantidade()+itemRemoveer.getQuantidade());
            daoP.merge(prod);

            carrinho.removerItem(itemRemoveer);

            daoK.merge(carrinho);

            fechaTransacao();
        }catch(Exception e){
            desfazerTransacao();
            //TODO add exception


        }

        carrinho.removerItem(itemRemoveer);

    }

}
