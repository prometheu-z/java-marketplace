package marketplace.view;

import marketplace.dao.VendedorDAO;
import marketplace.exceptions.EntradaInvalidaException;
import marketplace.exceptions.OperacaoVendaException;
import marketplace.exceptions.ProdutoInvalidoException;
import marketplace.exceptions.VendedorNuloExcception;
import marketplace.model.Produto;
import marketplace.model.Vendedor;
import marketplace.service.VendedorService;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VendedorView {

    private final DecimalFormat df = new DecimalFormat("000");

    public Vendedor criarVendedor(){


        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------ CADASTRO DE LOJA--------------");
            System.out.print("Qual o nome fantasia da sua loja:");
            String nome = ler.nextLine();
            //todo "validar" cnpj
            System.out.print("Qual o seu cnpj:");
            String cnpj = ler.nextLine();
            System.out.print("Crie uma senha:");
            String senha = ler.nextLine();
            System.out.println("\nLoja cadastrada!");

            return new Vendedor(nome, cnpj, senha);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }
    }

    public Vendedor alterarVendedor(Vendedor vendedor){
        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------- ALTERAÇÂO DE DADOS ----------------");

            System.out.println("(Apenas aperte enter se não quiser alterar um dado)");

            System.out.print("Qual o novo fantasia:");
            String nome = ler.nextLine();

            System.out.print("Qual a nova senha:");
            String senha = ler.nextLine();

            return new Vendedor(
                    nome.isEmpty() ? vendedor.getNomeLoja() : nome,
                    vendedor.getCnpj(),
                    senha.isEmpty() ? vendedor.getSenha() : senha);

        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }

    }

    public Produto criarProduto(){

        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------- CRIAR DE MERCADORIA ----------------");
            System.out.print("Qual o nome do produto:");
            String nome = ler.nextLine();

            System.out.print("Qual o preço do produto:");
            Double preco = ler.nextDouble();

            System.out.print("Qual a quantidade em estoque:");
            int estoque = ler.nextInt();

            return new Produto(nome, preco, estoque);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }


    }
    public Produto alterarProduto(Produto produto){
        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------- ALTERAÇÂO DE MERCADORIA ----------------");

            System.out.print("Qual sera o novo nome do produto:");
            String nome = ler.nextLine();

            System.out.print("Qual será seu novo valor unitário:");
            double valor = ler.nextDouble();
            ler.nextLine();

            System.out.print("Qual o seu estoque desse produto:");
            int esqtoque = ler.nextInt();

            if(esqtoque <= 0 || valor < 0){
                throw new OperacaoVendaException("Quantidade ou preço inválido para um produto");
            }

            return new Produto(nome, valor, esqtoque);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }

    }


    public void dashProdutos(Vendedor vendedor){
        VendedorDAO dao = new VendedorDAO();
        VendedorService vs = new VendedorService();


        Scanner ler = new Scanner(System.in);
        int quantPaginas = (int) Math.ceil((double) dao.numProdutosVendedor(vendedor) /4);
        int paginaAtual = 1;
        while (true){
            List<Produto> produtos = dao.itensDoEstoque(vendedor, (paginaAtual-1)*4, 4);
            if(paginaAtual == 1){
                System.out.println("\n-------------- Estoque "+vendedor.getNomeLoja().toUpperCase()+" -------------------");
            }
            System.out.println("\n\n          Pagina: "+paginaAtual+"/"+quantPaginas);

            for(Produto produto : produtos){

                System.out.println("=".repeat(40));
                System.out.println("Código: "+df.format(produto.getId_prod())+"        Vendas: "+produto.getVendas());
                System.out.println("Nome: "+produto.getNome()+"         Estoque: "+produto.getQuantidade());
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
                System.out.println("\n[3] alterar    [4] excluir      [5] sair");
                System.out.print("O que você quer fazer: ");

                try {
                    op = Integer.parseInt(ler.nextLine());
                    entradaValida = true;
                } catch (InputMismatchException e) {
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
                long cod;
                do {

                    System.out.print("Digite o codigo para alterar:");
                    try {
                        cod = Long.parseLong(ler.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: digite um valor válido");
                        cod = 0L;
                        continue;
                    }
                    try {
                        vs.atualizarProduto(vendedor.getId(), cod);
                    } catch (ProdutoInvalidoException | VendedorNuloExcception | OperacaoVendaException e) {
                        System.out.println(e.getMessage());
                        cod = 0;
                    }
                }while (cod == 0);

            }
            else if(op == 4){
                long cod;
                do {

                    System.out.print("Digite o codigo para excluir:");
                    try {
                        cod = Long.parseLong(ler.nextLine().trim());
                    } catch (InputMismatchException e) {
                        System.out.println("Erro: digite um valor válido");
                        cod = 0L;
                        continue;
                    }
                    try {
                        vs.excluirProduto(vendedor.getId(), cod);
                    } catch (ProdutoInvalidoException | VendedorNuloExcception | OperacaoVendaException e) {
                        System.out.println(e.getMessage());
                        cod = 0;
                    }
                }while (cod == 0);

            }
            else if (op == 5) {
                break;
            } else {
                System.out.println("Opção inválida ou indisponível.");
            }

        }

    }
}
