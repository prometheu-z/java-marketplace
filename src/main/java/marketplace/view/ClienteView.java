package marketplace.view;

import jakarta.persistence.NoResultException;
import marketplace.Main;
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

import static marketplace.Main.validaInput;

public class ClienteView {

    private final DecimalFormat df = new DecimalFormat("000");

    public Cliente login(Scanner ler){
        try{
            ClientesDAO dao = new ClientesDAO();

            System.out.println("------------ LOGIN --------------");
            System.out.print("Qual o seu email:");
            String email = ler.nextLine();
            System.out.print("Qual sua senha:");
            String senha = ler.nextLine();


            return dao.Pesquisar(email, senha);

        }catch (NoResultException e){
            throw new ClienteInvalidoException("Nenhum cliente encontrado");
        }
        catch (Exception e){
            throw new EntradaInvalidaException("entrada de valores inválidos");
        }
    }
    public Cliente criarCliente(Scanner ler){

        try {
            System.out.println("------------ CADASTRO DE CLIENTE --------------");
            System.out.print("Qual o seu nome:");
            String nome = ler.nextLine();
            System.out.print("Qual o seu email:");
            String email = ler.nextLine();
            System.out.print("Crie uma senha:");
            String senha = ler.nextLine();

            if(senha.length() <= 5){
                throw new EntradaInvalidaException("Senha não pode ser menor que 5 caracteres");
            }
            System.out.println("\nUsuário cadastrado!");

            return new Cliente(nome, email, senha);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de dados inválidos");
        }
    }

    public Cliente alterarCliente(Cliente cliente, Scanner ler){
        try {
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



    public void mostrarConta(Cliente cliente, Scanner ler){
        try{
            int opcao;
            boolean naoValido = true;

            ClientesDAO dao = new ClientesDAO();
            ClienteService service = new ClienteService();

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

            opcao = Main.validaInput(ler, new String[]{"[1] Alterar registro", "[2] Mostrar histórico", "[3] voltar"});

            if (opcao == 1) {
                while (naoValido) {
                    try {
                        cliente = service.atualizarCliente(cliente.getId(), ler);
                        naoValido = false;
                    } catch (ClienteInvalidoException | OperacaoCompraException e) {
                        System.out.println(e.getMessage());
                        naoValido = Main.tentarNovamente(ler);
                    }
                }

            }
            else if(opcao == 2){
                while (naoValido) {
                    try {
                        this.mostrarHistorico(cliente, ler);
                        naoValido = false;
                    } catch (CarrinhoNuloException e) {
                        System.out.println(e.getMessage());
                        naoValido = Main.tentarNovamente(ler);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Erro inesperado:"+e.getMessage());
        }
    }

    public void mostrarCarrinho(Cliente cliente, Scanner ler){
        ClientesDAO dao = new ClientesDAO();
        ClienteService service = new ClienteService();

        Compra compraAtiva = dao.compraAtiva(cliente);
        if(compraAtiva == null){
            throw new CarrinhoNuloException("Nenhuma compra ativa");
        }
        while (true){

            mostrarItens(compraAtiva.getItens());

            System.out.println("\n");

            int op;
            op = Main.validaInput(ler, new String[] {"[1] Finalizar compra","[2] Remover Produto", "[3] Sair"});


            if (op == 1) {
                service.finalizarCompra(cliente.getId());
                System.out.println("\nCompra finalizada");
                break;

            }  else if(op == 2){
                System.out.println("Qual o codigo do produto:");
                Long idProd = Long.parseLong(ler.nextLine());
                service.removerProduto(cliente.getId(), idProd);
                System.out.println("\nProduto removido");
            } else if(op == 3){
                break;
            }
            else {
                System.out.println("Opção inválida ou indisponível.");
            }

        }
    }
    public void mostrarHistorico(Cliente cliente, Scanner ler){
        ClientesDAO dao = new ClientesDAO();

        //NOTE:
        // quantidades de pagína na paginação
        // será um número inteiro, 1/4 do número de compras, ou seja, terá 4 itens por pagina

        int quantPaginas = (int) Math.ceil((double) dao.numCompras(cliente) /4);
        int paginaAtual = 1;
        while (true){
            List<Compra> compras = dao.getUltimasCompras(cliente,(paginaAtual-1)*4,4);
            if(compras.isEmpty()){
                throw new CarrinhoNuloException("nenhum Produto no carrinho");
            }
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

            int op = Main.validaInput(ler, new String[] {"[1] Voltar", "[2] Avançar ","[3] Gerar compom fiscal","[4] Sair"});


            if (op == 1) {
                if(paginaAtual > 1){
                    paginaAtual--;
                }
                else{
                    System.out.println("você já está na pagina 1");
                }
            }
            else if (op == 2) {
                if (paginaAtual < quantPaginas) {
                    paginaAtual++;
                } else {
                    System.out.println("Você já está na ultima pagina");
                }
            }
            else if(op == 3){
                try {
                    System.out.print("Qual o codigo da compra:");
                    Long idCupom = Long.parseLong(ler.nextLine());

                    gerarNotaFiscal(idCupom);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: digite um número válido.");
                }

            } else if (op == 4){
                break;
            }
            else {
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
                System.out.println(item.getVendedor().getNomeLoja().toUpperCase()+"\nCNPJ: "+
                        item.getVendedor().getCnpj());
                System.out.println("\nCodigo de Compra: "+df.format(item.getId_itemVenda()));
                System.out.println("\nCOD. QUANT. DESC.                     VALOR");
            }


            double valorReal = item.getValorAtual() * item.getQuantidade();
            System.out.printf(df.format(item.getProduto().getId_prod())+"  "+df.format(item.getQuantidade())+"    "+
                    item.getNomeProdAtual());
            for (int j = 0; j<25-item.getProduto().getNome().length(); j++){
                System.out.print(" ");
            }
            System.out.println("      "+valorReal);
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

