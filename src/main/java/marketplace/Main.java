package marketplace;

import marketplace.bot.BotRunner;
import marketplace.exceptions.*;
import marketplace.model.*;
import marketplace.service.ClienteService;
import marketplace.service.VendedorService;
import marketplace.view.ClienteView;
import marketplace.view.ProdutoView;
import marketplace.view.VendedorView;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        ClienteService clienteService = new ClienteService();
        VendedorService vendaService = new VendedorService();
        ClienteView clienteView = new ClienteView();
        VendedorView vendedorView = new VendedorView();
        ProdutoView produtoView = new ProdutoView();

        Scanner ler = new Scanner(System.in);
        boolean sistemaRodando = true;
        Object logado = null;

        boolean naoValido;

        int opcao;


        BotRunner botR = new BotRunner();

        while (sistemaRodando) {

            // ---> Area de registro
            opcao = validaInput(ler, new String[]{
                    "[1] Login",
                    "[2] Registrar Loja (Vendedor)",
                    "[3] Registrar Cliente",
                    "[4] Sair"
            });


            switch (opcao) {
                case 1:
                    naoValido = true;
                    while (naoValido) {
                        try {
                            int tipoUser = validaInput(ler, new String[]{"[1] Sou Cliente", "[2] Sou Vendedor", "[3] Voltar"});

                            if (tipoUser == 3) {
                                break;
                            }
                            if (tipoUser == 1) {
                                Cliente cliente = clienteView.login(ler);
                                System.out.println("Login bem sucedido, Bem vindo " + cliente.getNome());

                                botR.iniciarAutomacao();
                                logado = cliente;
                            } else {
                                Vendedor vendedor = vendedorView.login(ler);
                                System.out.println("Login bem sucedido, Bem vindo " + vendedor.getNomeLoja());

                                botR.iniciarAutomacao();
                                logado = vendedor;
                            }
                            naoValido = false;
                        } catch (EntradaInvalidaException | ClienteInvalidoException | VendedorNuloExcception e) {
                            System.out.println(e.getMessage());
                            naoValido = tentarNovamente(ler);

                        }
                    }
                    break;
                case 2:
                    naoValido = true;

                    while (naoValido) {
                        try {
                            logado = vendaService.criarVendedor(ler);
                            botR.iniciarAutomacao();
                            naoValido = false;
                        } catch (OperacaoVendaException e) {
                            System.out.println(e.getMessage());
                            naoValido = tentarNovamente(ler);
                        }
                    }
                    break;
                case 3:
                    naoValido = true;

                    while (naoValido) {
                        try {

                            logado = clienteService.criarCliente(ler);
                            botR.iniciarAutomacao();
                            naoValido = false;
                        } catch (OperacaoCompraException e) {
                            System.out.println(e.getMessage());
                            naoValido = tentarNovamente(ler);
                        }
                    }
                    break;
                case 4:
                    botR.pararAutomacao();
                    sistemaRodando = false;
                    break;

            }

            // ----> Area principal
            while (logado != null){
                // ----> Area do Cliente
                if(logado instanceof Cliente cliente){
                    System.out.println("---------------- Area principal ---------------");
                    opcao = validaInput(ler, new String[] {"[1] Minha conta", "[2] Carrinho ", "[3] Lista de produtos", "[4] Procurar produtos", "[5] Deslogar" });

                    switch (opcao){
                        case 1:

                            clienteView.mostrarConta(cliente, ler);
                            break;

                        case 2:
                            naoValido = true;
                            while (naoValido) {
                                try {
                                    clienteView.mostrarCarrinho(cliente, ler);
                                    naoValido = false;
                                } catch (CarrinhoNuloException e) {
                                    System.out.println(e.getMessage());
                                    naoValido = false;
                                }catch (NumberFormatException | ClienteInvalidoException | ProdutoInvalidoException | OperacaoCompraException e) {
                                    System.out.println(e.getMessage());
                                    naoValido = tentarNovamente(ler);
                                }
                            }
                            break;


                        case 3:
                            naoValido = true;
                            while (naoValido) {
                                try {
                                    produtoView.exibirCatalogo(cliente, ler);
                                    break;
                                } catch (ProdutoInvalidoException e) {
                                    System.out.println(e.getMessage());
                                    naoValido = tentarNovamente(ler);
                                }
                            }

                            break;
                        case 4:
                            naoValido = true;
                            while (naoValido) {
                                System.out.print("Qual produtos deseja proucurar:");
                                try {
                                    produtoView.pesquisaCatalogo(cliente, ler.nextLine(), ler);
                                    break;
                                } catch (ProdutoInvalidoException e) {
                                    System.out.println(e.getMessage());
                                    naoValido = tentarNovamente(ler);
                                }
                            }

                            break;

                        case 5:
                            logado = null;
                            break;

                    }

                }

                // ----->  Area do Vendedor
                if(logado instanceof Vendedor vendedor){
                    System.out.println("---------------- Area principal ---------------");
                    opcao = validaInput(ler, new String[] {"[1] Minha conta", "[2] Criar Produto ", "[3] Ver estoque", "[4] Deslogar" });

                    switch (opcao){
                        case 1:
                            vendedorView.mostrarConta(vendedor, ler);
                            break;

                        case 2:
                            naoValido = true;
                            while (naoValido) {
                                try {
                                    vendaService.criarProduto(vendedor.getId(), ler);
                                    naoValido = false;
                                } catch (VendedorNuloExcception | ProdutoInvalidoException | OperacaoVendaException e) {
                                    System.out.println(e.getMessage());
                                    naoValido = tentarNovamente(ler);
                                }
                            }
                            break;


                        case 3:
                            vendedorView.dashProdutos(vendedor, ler);
                            break;

                        case 4:
                            logado = null;
                            break;

                    }
                }

            }

        }
    }

    public static int validaInput(Scanner ler, String[] opcoes){
        boolean naoValido = true;
        int variavel = 0;
        while (naoValido) {
            for (String opcao : opcoes) {
                System.out.println(opcao);
            }
            System.out.print("Escolha: ");
            try {
                variavel = Integer.parseInt(ler.nextLine().trim());
                if(variavel > 0 && variavel <= opcoes.length){
                    naoValido = false;
                } else {
                    System.out.println("Opção inválida! Escolha entre 1 e " + opcoes.length);
                }

            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Erro: Digite apenas números válidos.");
            }
        }
        return variavel;
    }

    public static boolean tentarNovamente(Scanner ler){
        int opcao;
        opcao = validaInput(ler, new String[]{"[1] Tentar novamente", "[2] Voltar"});
        return opcao != 2;
    }
}


