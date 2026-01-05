package marketplace.service;

import jakarta.persistence.EntityManager;
import marketplace.dao.*;
import marketplace.exceptions.EntradaInvalidaException;
import marketplace.exceptions.OperacaoVendaException;
import marketplace.exceptions.ProdutoInvalidoException;
import marketplace.exceptions.VendedorNuloExcception;
import marketplace.model.Produto;
import marketplace.model.Vendedor;
import marketplace.view.VendedorView;

public class VendedorService {
    private EntityManager em;
    private ClientesDAO daoC;
    private CompraDAO daoK;
    private VendedorDAO daoV;
    private ProdutoDAO daoP;

    public VendedorService() {
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

    public Vendedor criarVendedor(){
        abreTransacao();

        VendedorView view = new VendedorView();
        try {
            Vendedor vendedor = view.criarVendedor();

            daoV.persistir(vendedor);

            fechaTransacao();

            System.out.println("Loja "+vendedor.getNomeLoja()+" adicionada");

            return vendedor;

        }catch (EntradaInvalidaException e){
            System.out.println("Operação cancelada: "+e.getMessage() );
            desfazerTransacao();
        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e){
            desfazerTransacao();

            throw new OperacaoVendaException("Não foi possível criar a loja", e);
        }
        return null;
    }
    public void alterarVendedor(Long idVendedor){
        abreTransacao();

        VendedorView view = new VendedorView();
        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                throw new VendedorNuloExcception("Vendedor de id: "+idVendedor+", não encontrado");
            }

            Vendedor novoVendedor = view.alterarVendedor(vendedor);

            vendedor.setSenha(novoVendedor.getSenha());
            vendedor.setNomeLoja(novoVendedor.getNomeLoja());

            daoV.merge(vendedor);

            fechaTransacao();

            System.out.println("Loja "+vendedor.getNomeLoja()+" atualizada");


        }catch (EntradaInvalidaException e){
            System.out.println("Operação cancelada: "+e.getMessage() );
            desfazerTransacao();
        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e){
            desfazerTransacao();

            throw new OperacaoVendaException("Não foi possível alterar a loja", e);
        }
    }

    public void criarProduto(Long idVendedor){

        abreTransacao();

        VendedorView view = new VendedorView();
        try {

            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                throw new VendedorNuloExcception("Vendedor de id: "+idVendedor+", não encontrado");
            }

            Produto produto = view.criarProduto();
            vendedor.adicionarEstoque(produto);
            daoP.persistir(produto);

            daoP.merge(produto);

            fechaTransacao();
        } catch (OperacaoVendaException | EntradaInvalidaException e){
            System.out.println("Operação cancelada: "+e.getMessage());
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

    public void atualizarProduto(Long idVendedor, Long idProduto){
        abreTransacao();
        VendedorView view = new VendedorView();
        try {
            Vendedor vendedor = daoV.buscarPorId(idVendedor);
            if(vendedor == null){
                throw new VendedorNuloExcception("Vendedor de id: "+idVendedor+", não encontrado");
            }

            Produto produto = daoP.buscarPorId(idProduto);
            if(produto == null || !produto.isAtivo()){
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

            System.out.println("Produto "+produto.getNome()+" atualizado");
            fechaTransacao();

        }  catch (EntradaInvalidaException e){
            desfazerTransacao();
            System.out.println("Operação cancelada: "+e.getMessage());

        } catch (RuntimeException e){
            desfazerTransacao();

            throw e;
        } catch (Exception e) {
            desfazerTransacao();

            throw new OperacaoVendaException("Não foi possivel alterar o produto", e);

        }

    }


}
