package marketplace.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import marketplace.dao.*;
import marketplace.exceptions.EntradaInvalidaException;
import marketplace.exceptions.OperacaoVendaException;
import marketplace.exceptions.ProdutoInvalidoException;
import marketplace.exceptions.VendedorNuloExcception;
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
            if(qtd <= 0 || preco < 0){
                throw new OperacaoVendaException("Quantidade ou preço inválido para um produto");
            }

            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                throw new VendedorNuloExcception("Vendedor de id: "+idVendedor+", não encontrado");
            }



            Produto produto = new Produto(nomeProd, preco, qtd);
            vendedor.adicionarEstoque(produto);
            daoP.persistir(produto);

            daoP.merge(produto);

            fechaTransacao();
        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {
            desfazerTransacao();

            throw new OperacaoVendaException("Erro ao criar um produto",e);
        }
    }
    public void excluirProduto(Long idVendedor, Long idProduto){
        abreTransacao();

        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                throw new VendedorNuloExcception("Vendedor de id: "+idVendedor+", não encontrado");
            }

            Produto produto = daoP.buscarPorId(idProduto);
            if(produto == null){
                throw new ProdutoInvalidoException("Produto de id: "+  idProduto+", não encontrado");
            }
            if(!produto.getVendedor().getId().equals(vendedor.getId())){
                throw new ProdutoInvalidoException("Este produto de id: "+idProduto+", não pertence à esta loja");
            }

            vendedor.removerEstoque(produto);

            daoP.remover(produto);

            fechaTransacao();

        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e) {
            desfazerTransacao();

            throw new OperacaoVendaException("Erro ao excluir o produto", e);

        }


    }

    public void alterarProduto(Long idVendedor, Long idProduto){
        abreTransacao();
        VendedorView view = new VendedorView();

        //todo criar logica pra decidir se deve criar um novo produto pra mudanças graves ou manter para mudanças minimas de preço e nome

        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                throw new VendedorNuloExcception("Vendedor de id: "+idVendedor+", não encontrado");
            }

            Produto produto = daoP.buscarPorId(idProduto);
            if(produto == null){
                throw new ProdutoInvalidoException("Produto de id: "+  idProduto+", não encontrado");
            }

            if(!produto.getVendedor().getId().equals(vendedor.getId())){
                throw new ProdutoInvalidoException("Este produto de id: "+idProduto+", não pertence à esta loja");
            }

            Produto produtoAlterado = view.alterarProduto(produto);


            produto.setNome(produtoAlterado.getNome());
            produto.setValorUnitario(produtoAlterado.getValorUnitario());
            produto.setQuantidade(produtoAlterado.getQuantidade());

            daoP.merge(produto);

            fechaTransacao();

        }  catch (EntradaInvalidaException e){
            desfazerTransacao();
            System.out.println("Operação cancelada: "+e.getMessage());

        }
        catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e) {
            desfazerTransacao();

            throw new OperacaoVendaException("Não foi possivel alterar o produto", e);

        }

    }


}
