package marketplace;

import jakarta.persistence.criteria.CriteriaBuilder;
import marketplace.exceptions.*;
import marketplace.model.*;
import marketplace.service.ClienteService;
import marketplace.service.VendedorService;
import marketplace.view.ClienteView;
import marketplace.view.ProdutoView;
import marketplace.view.VendedorView;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        ClienteService compra = new ClienteService();
        VendedorService venda = new VendedorService();
        ClienteView clienteView = new ClienteView();
        VendedorView vendedorView = new VendedorView();
        ProdutoView produtoView = new ProdutoView();

        Scanner ler = new Scanner(System.in);
        boolean sistemaRodando = true;

        while (sistemaRodando) {

            // ---> Area de registro
            int register = 0;
            register = validaInput(ler, new String[] {
                    "[1] Login",
                    "[2] Registrar Loja (Vendedor)",
                    "[3] Registrar Cliente",
                    "[4] Sair"
            });


            boolean registrando;
            Object logado = null;
            switch (register) {
                case 1:
                    registrando = true;
                    while (registrando) {
                        try {
                            int tipoUser = validaInput(ler, new String[] {"[1] Sou Cliente", "[2] Sou Vendedor", "[3] Voltar"});

                            if (tipoUser == 3) {
                                registrando = false;
                                break;
                            }
                            if (tipoUser == 1) {
                                Cliente cliente = clienteView.login();
                                System.out.println("Login bem sucedido, Bem vindo " + cliente.getNome());

                                logado = cliente;
                                registrando = false;
                            } else {
                                Vendedor vendedor = vendedorView.login();
                                System.out.println("Login bem sucedido, Bem vindo " + vendedor.getNomeLoja());

                                logado = vendedor;
                                registrando = false;
                            }
                        } catch (EntradaInvalidaException | ClienteInvalidoException | VendedorNuloExcception e) {
                            System.out.println("Erro:" + e.getMessage());
                            int opErro = validaInput(ler, new String[] {"[1] Tentar novamente", "[2] Voltar ao Menu Principal"});
                            if (opErro == 2) {
                                registrando = false;
                            }

                        }
                    }
                    break;
                case 2:
                    registrando = true;

                    while (registrando){
                        try {

                            logado = vendedorView.criarVendedor();
                            registrando = false;
                        } catch (EntradaInvalidaException e){
                            System.out.println("Erro:"+e.getMessage());
                            int opErro = validaInput(ler, new String[] {"[1] Tentar novamente", "[2] Voltar ao Menu Principal"});
                            if (opErro == 2) {
                                registrando = false;
                            }
                        }
                    }
                case 3:
                    registrando = true;

                    while (registrando){
                        try {

                            logado = clienteView.criarCliente();
                            registrando = false;
                        } catch (EntradaInvalidaException e){
                            System.out.println("Erro:"+e.getMessage());
                            int opErro = validaInput(ler, new String[] {"[1] Tentar novamente", "[2] Voltar ao Menu Principal"});
                            if (opErro == 2) {
                                registrando = false;
                            }
                        }
                    }

            }


        }
    }

    private static int validaInput(Scanner ler, String[] opcoes){
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
}


