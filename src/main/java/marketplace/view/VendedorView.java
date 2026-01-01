package marketplace.view;

import marketplace.dao.VendedorDAO;
import marketplace.exceptions.EntradaInvalidaException;
import marketplace.model.Cliente;
import marketplace.model.Produto;
import marketplace.model.Vendedor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class VendedorView {

    private final DecimalFormat df = new DecimalFormat("000");

    public Vendedor criarVendedor(){


        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------ Cadastro --------------");
            System.out.println("Qual o nome fantasia da sua loja:");
            String nome = ler.nextLine();
            //todo "validar" cnpj
            System.out.println("Qual o seu cnpj:");
            String cnpj = ler.nextLine();
            System.out.println("Crie uma senha:");
            String senha = ler.nextLine();
            System.out.println("\n  Loja cadastrado!");

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

    public Produto alterarProduto(Produto produto){
        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------- ALTERAÇÂO DE MERCADORIA ----------------");

            System.out.print("Qual sera o novo nome do produto:");
            String nome = ler.nextLine();

            System.out.print("Qual será seu novo valor unitário:");
            Double valor = ler.nextDouble();
            ler.nextLine();

            System.out.print("Qual o seu estoque desse produto:");
            int esqtoque = ler.nextInt();

            return new Produto(nome, valor, esqtoque);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }

    }

    public void dashProdutos(Vendedor vendedor){
        VendedorDAO dao = new VendedorDAO();


        Scanner ler = new Scanner(System.in);
        int quantPaginas = (int) Math.ceil((double) dao.numProdutos(vendedor) /4);
        int paginaAtual = 1;
        while (true){
            System.out.println(quantPaginas);
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
            if(paginaAtual != 1){
                System.out.print("[1] voltar      ");
            }
            if(paginaAtual != quantPaginas){
                System.out.print("[2] avançar");
            }
            //todo opcoes pra adicionar ou remover produtos
            System.out.println("\n[3] sair");
            System.out.print("O que você quer fazer:");
            int op = ler.nextInt();
            ler.nextLine();

            if(op == 1){
                paginaAtual--;
            }
            else if(op == 2){
                paginaAtual++;
            }
            else if(op == 3){
                break;
            }
            else {
                System.out.println("Operação inválida, saindo...");
                break;
            }
        }

    }
}
