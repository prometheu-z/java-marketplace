package marketplace.view;

import marketplace.Main;
import marketplace.dao.ClientesDAO;
import marketplace.dao.ProdutoDAO;
import marketplace.exceptions.ClienteInvalidoException;
import marketplace.exceptions.OperacaoCompraException;
import marketplace.exceptions.ProdutoInvalidoException;
import marketplace.model.Cliente;
import marketplace.model.Compra;
import marketplace.model.Produto;
import marketplace.service.ClienteService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProdutoView {
    private final DecimalFormat df = new DecimalFormat("000");


    public void pesquisaCatalogo(Cliente cliente, String pesquisa,Scanner ler) {
        ProdutoDAO dao = new ProdutoDAO();
        ClienteService service = new ClienteService();

        int paginaAtual = 1;

        while (true) {
            List<Produto> produtos = dao.pesquisarProdutos((paginaAtual - 1) * 4, 4, pesquisa);
            if(produtos.isEmpty()){
                throw new ProdutoInvalidoException("Nenhum produto encontrado para: "+ pesquisa);
            }
            long totalProdutos = dao.numProdutos();

            int quantPaginas = Math.max(1, (int) Math.ceil((double) totalProdutos / 4));

            if (paginaAtual > quantPaginas) {
                paginaAtual = 1;
                continue;
            }

            if (paginaAtual == 1) {
                System.out.println("\n-------------- Produtos relacionados a: "+pesquisa+" -------------------");
            }
            System.out.println("\n\n          Pagina: " + paginaAtual + "/" + quantPaginas);


            for (Produto produto : produtos) {
                System.out.println("=".repeat(40));
                System.out.print("Nome: "+produto.getNome());
                System.out.println("        Valor: " + produto.getValorUnitario());
                System.out.print("Código: " + df.format(produto.getId_prod()));
                System.out.println("        Vendidos: " + produto.getVendas());
                System.out.println("=".repeat(40));
            }

            System.out.println("\n");

            int op = 0;
            op = Main.validaInput(ler, new String[] {"[1] Voltar", "[2] avançar", "[3] Mudar pesquisa",
                    "[4] adicionar produto ao carrinho",
                    "[5] Sair"
            });




            if (op == 1) {
                if(paginaAtual > 1){
                    paginaAtual--;
                }
                else{
                    System.out.println("você já está na pagina 1");
                }
            }
            else if (op == 2) {
                if(paginaAtual < quantPaginas){
                    paginaAtual++;
                }
                else {
                    System.out.println("Você já está na ultima pagina");
                }
            } else if (op == 3) {
                System.out.print("Qual a nova pesquisa: ");
                pesquisa= ler.nextLine();

            } else if(op == 4){
                boolean naoValido = true;
                while (naoValido) {
                    try {
                        System.out.print("Qual o codigo do produto:");
                        Long codProd = Long.parseLong(ler.nextLine().trim());
                        System.out.print("Qual o quantidade:");
                        int quant = Integer.parseInt(ler.nextLine().trim());

                        service.adicionarProduto(cliente.getId(), codProd, quant);
                        naoValido = false;
                    } catch (NumberFormatException | ClienteInvalidoException | ProdutoInvalidoException |
                             OperacaoCompraException e) {
                        System.out.println(e.getMessage());
                        naoValido = Main.tentarNovamente(ler);
                    }
                }
            }
            else if (op == 5) {
                break;
            } else {
                System.out.println("Opção inválida ou indisponível.");
            }
        }
    }

    public void exibirCatalogo(Cliente cliente, Scanner ler) {
        ProdutoDAO dao = new ProdutoDAO();
        ClienteService service = new ClienteService();

        int paginaAtual = 1;
        int filtro = 0;
        double precoMin = 0.0;
        int vendaMin = 5;

        while (true) {
            List<Produto> produtos = new ArrayList<>();
            long totalProdutos = 0;

            switch (filtro) {
                case 0:
                    produtos = dao.listarProdutos((paginaAtual - 1) * 4, 4);
                    totalProdutos = dao.numProdutos();
                    break;
                case 1:
                    produtos = dao.listarProdutosAlph((paginaAtual - 1) * 4, 4);
                    totalProdutos = dao.numProdutos();
                    break;
                case 2:
                    produtos = dao.listarProdutosValMin((paginaAtual - 1) * 4, 4, precoMin);
                    totalProdutos = dao.numProdutosValMin(precoMin);
                    break;
                case 3:
                    produtos = dao.listarProdutosVendas((paginaAtual - 1) * 4, 4, vendaMin);
                    totalProdutos = dao.numProdutosVendas(vendaMin);
                    break;
            }
            if(produtos.isEmpty()){
                throw new ProdutoInvalidoException("Nenhum produto encontrado");
            }
            int quantPaginas = Math.max(1, (int) Math.ceil((double) totalProdutos / 4));

            if (paginaAtual > quantPaginas) {
                paginaAtual = 1;
                continue;
            }

            if (paginaAtual == 1) {
                System.out.println("\n-------------- Produtos -------------------");
            }
            System.out.println("\n\n          Pagina: " + paginaAtual + "/" + quantPaginas);


            for (Produto produto : produtos) {
                System.out.println("=".repeat(40));
                System.out.print("Nome: "+produto.getNome());
                System.out.println("        Valor: " + produto.getValorUnitario());
                System.out.print("Código: " + df.format(produto.getId_prod()));
                System.out.println("        Vendidos: " + produto.getVendas());
                System.out.println("Disponíveis: "+produto.getQuantidade());
                System.out.println("=".repeat(40));
            }

            System.out.println("\n");

            int op = 0;
            op = Main.validaInput(ler, new String[] {"[1] Voltar", "[2] avançar", "[3] adicionar filtro",
                    "[4] adicionar produto ao carrinho",
                    "[5] Sair"
            });



            if (op == 1) {
                if(paginaAtual > 1){
                    paginaAtual--;
                }
                else{
                    System.out.println("você já está na pagina 1");
                }
            }
            else if (op == 2) {
                if(paginaAtual < quantPaginas){
                    paginaAtual++;
                }
                else {
                    System.out.println("Você já está na ultima pagina");
                }
            } else if (op == 3) {
                int novoFiltro = 0;
                do {
                    System.out.println("\n=== ESCOLHA O FILTRO ===");
                    System.out.println("[0] Limpar Filtros (Ver tudo)");
                    System.out.println("[1] Ordem alfabética");
                    System.out.println("[2] Preço mínimo");
                    System.out.println("[3] Mais vendidos");
                    System.out.print("Qual o filtro: ");
                    try {
                        novoFiltro = Integer.parseInt(ler.nextLine());
                    } catch (NumberFormatException e) {
                        novoFiltro = -1;
                    }
                } while (novoFiltro < 0 || novoFiltro > 3);

                filtro = novoFiltro;

                if(filtro == 2){
                    do {
                        System.out.print("Qual o preço mínimo:");
                        try {
                            String entrada = ler.nextLine().replace(",", ".");
                            precoMin = Double.parseDouble(entrada);
                        } catch (NumberFormatException e) {
                            System.out.println("Erro: digite um valor válido");
                            precoMin = 0.0;
                        }
                    }while (precoMin<0);

                }
                else if (filtro == 3) {
                    do {
                        System.out.print("Mínimo de vendas para exibir: ");
                        try {
                            vendaMin = Integer.parseInt(ler.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Erro: digite um número inteiro.");
                            vendaMin = -1;
                        }
                    } while (vendaMin < 0);
                }
                paginaAtual = 1;
            } else if (op == 4) {
                boolean naoValido = true;
                while (naoValido) {
                    try {
                        System.out.print("Qual o codigo do produto:");
                        Long codProd = Long.parseLong(ler.nextLine().trim());
                        System.out.print("Qual o quantidade:");
                        int quant = Integer.parseInt(ler.nextLine().trim());

                        service.adicionarProduto(cliente.getId(), codProd, quant);
                        naoValido = false;
                    } catch (NumberFormatException | ClienteInvalidoException | ProdutoInvalidoException |
                             OperacaoCompraException e) {
                        System.out.println(e.getMessage());
                        naoValido = Main.tentarNovamente(ler);
                    }
                }
            }
            else if(op == 5){
                break;
            }
            else {
                System.out.println("Opção inválida ou indisponível.");
            }
        }
    }
}
