package marketplace.view;

import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.exceptions.CarrinhoNuloException;
import marketplace.exceptions.ClienteInvalidoException;
import marketplace.exceptions.EntradaInvalidaException;
import marketplace.exceptions.OperacaoCompraException;
import marketplace.model.Cliente;
import marketplace.model.Compra;
import marketplace.model.ItemCompra;
import marketplace.model.Produto;
import marketplace.service.ClienteService;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ClienteView {

    private final DecimalFormat df = new DecimalFormat("000");

    public Cliente login(){
        try{
            ClientesDAO dao = new ClientesDAO();
            Scanner ler = new Scanner(System.in);

            System.out.println("------------ LOGIN --------------");
            System.out.print("Qual o seu email:");
            String email = ler.nextLine();
            System.out.print("Qual sua senha:");
            String senha = ler.nextLine();

            Cliente cliente = dao.Pesquisar(email, senha);

            if(cliente == null){
                throw new ClienteInvalidoException("cliente não encontrado");
            }
            return cliente;

        } catch (Exception e){
            throw new EntradaInvalidaException("entrada de valores inválidos");
        }
    }
    public Cliente criarCliente(){

        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------ CADASTRO DE CLIENTE --------------");
            System.out.print("Qual o seu nome:");
            String nome = ler.nextLine();
            System.out.print("Qual o seu email:");
            String email = ler.nextLine();
            System.out.print("Crie uma senha:");
            String senha = ler.nextLine();
            System.out.println("\nUsuário cadastrado!");

            return new Cliente(nome, email, senha);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de dados inválidos");
        }
    }

    public Cliente alterarCliente(Cliente cliente){
        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------- ALTERAÇÂO DE DADOS ----------------");

            System.out.println("(Apenas aperte enter se não quiser alterar um dado)");

            System.out.print("Qual o novo nome:");
            String nome = ler.nextLine();

            System.out.print("Qual o novo email:");
            String email = ler.nextLine();

            System.out.print("Qual a nova senha:");
            String senha = ler.nextLine();

            return new Cliente(
                    nome.isEmpty() ? cliente.getNome() : nome,
                    email.isEmpty() ? cliente.getEmail() : email,
                    senha.isEmpty() ? cliente.getSenha() : senha);

        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de dados inválidos");
        }

    }



    public void mostrarConta(Cliente cliente){
        try{
            ClientesDAO dao = new ClientesDAO();

            System.out.println("Nome: "+cliente.getNome()+"        Cod: "+cliente.getId());
            System.out.println("Email: "+cliente.getEmail());
            System.out.println("Senha: "+cliente.getSenha());

            List<Compra> compras = dao.getUltimasCompras(cliente, 0, 2);

            System.out.println("Historico de compras:");
            for (Compra compra : compras){
                System.out.println("-----------------------------");
                System.out.println("Compra ativa:"+compra.getCompraAtiva()+"         Id:"+compra.getId_compra());
                System.out.println("Valor: "+compra.getValorTotal()+"      Itens:"+compra.getItens().size());
            }
            System.out.println("-----------------------------");



        }catch (Exception e){
            System.out.println("Erro inesperado:"+e.getMessage());
        }
    }

    public void mostrarCarrinho(Cliente cliente){
        ClientesDAO dao = new ClientesDAO();
        Scanner ler = new Scanner(System.in);
        ClienteService service = new ClienteService();

        Compra compraAtiva = dao.compraAtiva(cliente);
        if(compraAtiva == null){
            throw new CarrinhoNuloException("Nenhuma compra ativa");
        }
        while (true){

            mostrarItens(compraAtiva.getItens());

            System.out.println("\n");

            int op = 0;
            boolean entradaValida = false;

            while (!entradaValida) {
                System.out.println("\n[1] gerar cupom fiscal\n[2] Finalizar compra\n[3] Remover Produto\n[4] Sair");
                System.out.print("O que você quer fazer: ");

                try {
                    op = Integer.parseInt(ler.nextLine());
                    entradaValida = true;
                } catch (InputMismatchException e) {
                    System.out.println("Erro: digite um número válido.");
                }
            }

            if(op == 1 ){
                System.out.println("Qual o codigo da compra:");
                Long idCupom = ler.nextLong();
                ler.nextLine();
                gerarNotaFiscal(idCupom);
            }
            else if (op == 2) {
                try {
                    service.finalizarCompra(cliente);
                } catch (CarrinhoNuloException |  OperacaoCompraException e) {
                    System.out.println(e.getMessage());
                }
            }  else if(op == 3){
                System.out.println("Qual o codigo do produto:");
                Long idProd = ler.nextLong();
                ler.nextLine();
                service.removerProduto(cliente.getId(), idProd);
            } else if(op == 4){
                break;
            }
            else {
                System.out.println("Opção inválida ou indisponível.");
            }

        }
    }
    public void mostrarHistorico(Cliente cliente){
        ClientesDAO dao = new ClientesDAO();
        Scanner ler = new Scanner(System.in);
        int quantPaginas = (int) Math.ceil((double) dao.numCompras(cliente) /4);
        int paginaAtual = 1;
        while (true){
            List<Compra> compras = dao.getUltimasCompras(cliente,(paginaAtual-1)*4,4);
            if(paginaAtual == 1){
                System.out.println("\n-------------- Compras -------------------");
            }
            System.out.println("\n\n          Pagina: "+paginaAtual+"/"+quantPaginas);

            for (Compra compra : compras){
                System.out.println("=".repeat(40));
                System.out.print("valor: "+compra.getValorTotal());
                System.out.println("        Código: "+df.format(compra.getId_compra()));
                System.out.println("\nData: "+compra.getHorario().getDayOfMonth()+"/"+
                        compra.getHorario().getMonthValue()+"/"+
                        compra.getHorario().getYear()+"  "+
                        compra.getHorario().getHour()+":"+
                        compra.getHorario().getMinute()+":"+
                        compra.getHorario().getSecond());
                System.out.println("=".repeat(40));
            }

            System.out.println("\n");

            int op = 0;
            boolean entradaValida = false;

            while (!entradaValida) {
                if (paginaAtual > 1) {
                    System.out.print("[1] voltar      ");
                }
                if (paginaAtual < quantPaginas) {
                    System.out.print("[2] avançar");
                }
                System.out.println("\n[3] gerar cupom fiscal ");
                System.out.print("O que você quer fazer: ");

                try {
                    op = Integer.parseInt(ler.nextLine());
                    entradaValida = true;
                } catch (NumberFormatException e) {
                    System.out.println("Erro: digite um número válido.");
                }
            }

            if(op == 1 && paginaAtual > 1){
                paginaAtual--;
            }
            else if(op == 2 && paginaAtual < quantPaginas){
                paginaAtual++;
            }
            else if(op == 3){
                try {
                    System.out.println("Qual o codigo da compra:");
                    Long idCupom = Long.parseLong(ler.nextLine());

                    gerarNotaFiscal(idCupom);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: digite um número válido.");
                }

            } else {
                System.out.println("Opção inválida ou indisponível.");
            }

        }




    }
    private void mostrarItens(List<ItemCompra> i){
        Iterator<ItemCompra> itens = i.iterator();
        Long idVendedor = (long) -1;
        while (itens.hasNext()){
            ItemCompra item = itens.next();


            if(!Objects.equals(item.getVendedor().getId(), idVendedor)){
                System.out.println("\n----------------------------");
                System.out.println(item.getVendedor().getNomeLoja().toUpperCase()+"\ncnpj: "+
                        item.getVendedor().getCnpj());
                System.out.println("\nCOD. QUANT. DESC.                     VALOR");
            }



            System.out.printf(df.format(item.getId_itemVenda())+"  "+df.format(item.getQuantidade())+"    "+
                    item.getNomeProdAtual());
            for (int j = 0; j<25-item.getProduto().getNome().length(); j++){
                System.out.print(" ");
            }
            System.out.println(item.getSubTotal());
            idVendedor = item.getVendedor().getId();
        }

        System.out.println("\n----------------------------");



    }
    public void gerarNotaFiscal(Long idCompra){
        CompraDAO dao = new CompraDAO();

        Compra compra = dao.buscarPorId(idCompra);

        if(compra == null){
            throw new CarrinhoNuloException("Compra não encontrada");
        }

        System.out.println("----------------------------");
        System.out.println("JAVA MARKETPLACE\nRua tal s/n\n");
        System.out.println("cnpj: 89.455.000/003-00\nCOD:"+df.format(compra.getId_compra()));
        System.out.println("Data: "+
                compra.getHorario().getDayOfMonth()+"/" +
                compra.getHorario().getMonthValue()+"/"+
                compra.getHorario().getYear()+"   "+
                compra.getHorario().getHour()+":"+
                compra.getHorario().getMinute()+":"+
                compra.getHorario().getSecond());

        mostrarItens(compra.getItens());

        System.out.print("TOTAL");
        for (int j = 0; j<32; j++){
            System.out.print(" ");
        }
        System.out.println(compra.getValorTotal());

    }
}

