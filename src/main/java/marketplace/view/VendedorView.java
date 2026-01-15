package marketplace.view;

import jakarta.persistence.NoResultException;
import marketplace.Main;
import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.dao.ProdutoDAO;
import marketplace.dao.VendedorDAO;
import marketplace.exceptions.*;
import marketplace.model.*;
import marketplace.service.VendedorService;

import java.text.DecimalFormat;
import java.util.*;

public class VendedorView {

    private final DecimalFormat df = new DecimalFormat("000");

    public Vendedor login(Scanner ler){
        try{
            VendedorDAO dao = new VendedorDAO();

            System.out.println("------------ LOGIN --------------");
            System.out.print("Qual o seu cnpj:");
            String email = ler.nextLine();
            System.out.print("Qual sua senha:");
            String senha = ler.nextLine();


            return dao.Pesquisar(email, senha);

        } catch (NoResultException e){
            throw new VendedorNuloExcception("Nenhum vendedor encontrado");
        } catch (Exception e) {
            throw new EntradaInvalidaException("Erro inesperado, tente novamente");
        }
    }

    public Vendedor criarVendedor(Scanner ler){



        try {
            System.out.println("------------ CADASTRO DE LOJA--------------");
            System.out.print("Qual o nome fantasia da sua loja:");
            String nome = ler.nextLine();
            //todo "validar" cnpj
            System.out.print("Qual o seu cnpj:");
            String cnpj = ler.nextLine();
            System.out.print("Crie uma senha:");
            String senha = ler.nextLine();
            System.out.print("Qual o seu nicho:");
            String nicho = ler.nextLine();
            System.out.println("\nLoja cadastrada!");

            return new Vendedor(nome, senha, cnpj, nicho);

        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }
    }

    public Vendedor alterarVendedor(Vendedor vendedor, Scanner ler){
        try {
            System.out.println("------------- ALTERAÇÂO DE DADOS ----------------");

            System.out.println("(Apenas aperte enter se não quiser alterar um dado)");

            System.out.print("Qual o novo fantasia:");
            String nome = ler.nextLine();

            System.out.print("Qual a nova senha:");
            String senha = ler.nextLine();

            return new Vendedor(
                    nome.isEmpty() ? vendedor.getNomeLoja() : nome,
                    senha.isEmpty() ? vendedor.getSenha() : senha,
                    vendedor.getCnpj(),
                    vendedor.getNicho());

        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }

    }

    public Produto criarProduto(Scanner ler){

        try {
            System.out.println("------------- CRIAR DE MERCADORIA ----------------");
            System.out.print("Qual o nome do produto:");
            String nome = ler.nextLine();

            System.out.print("Qual o preço do produto:");
            Double preco = Double.parseDouble(ler.nextLine().trim());

            System.out.print("Qual a quantidade em estoque:");
            int estoque = Integer.parseInt(ler.nextLine().trim());

            return new Produto(nome, preco, estoque);
        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de valores inválidos");
        }


    }
    public Produto alterarProduto(Produto produto, Scanner ler){
        try {
            System.out.println("------------- ALTERAÇÂO DE MERCADORIA ----------------");

            System.out.print("Qual sera o novo nome do produto:");
            String nome = ler.nextLine();

            System.out.print("Qual será seu novo valor unitário:");
            double valor = Double.parseDouble(ler.nextLine().trim());

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

    public void mostrarConta(Vendedor vendedor, Scanner ler){
        try{
            CompraDAO dao = new CompraDAO();
            VendedorService service = new VendedorService();
            int opcao;
            boolean naoValido = true;

            System.out.println("---------------------------");

            System.out.println("Loja: "+vendedor.getNomeLoja()+"        Cod: "+vendedor.getId());
            System.out.println("cnpj: "+vendedor.getCnpj());
            System.out.println("Senha: "+vendedor.getSenha());
            System.out.println("Produtos vendidos: " +dao.totalVendasDoVendedor(vendedor.getId()));

            System.out.println("---------------------------");

            opcao = Main.validaInput(ler, new String[]{"[1] Alterar registro", "[2] Mostrar histórico", "[3] voltar"});

            if (opcao == 1) {
                while (naoValido) {
                    try {
                        vendedor = service.alterarVendedor(vendedor.getId(), ler);
                        naoValido = false;
                    } catch (VendedorNuloExcception | OperacaoVendaException e) {
                        System.out.println(e.getMessage());
                        naoValido = Main.tentarNovamente(ler);
                    }
                }
            }
            else if(opcao == 2){
                while (naoValido) {
                    try {
                        this.mostrarHistorico(vendedor, ler);
                        naoValido = false;
                    } catch (OperacaoVendaException e) {
                        System.out.println(e.getMessage());
                        naoValido = Main.tentarNovamente(ler);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Erro inesperado:"+e.getMessage());
        }
    }

    public void mostrarHistorico(Vendedor vendedor, Scanner ler) {
        CompraDAO dao = new CompraDAO();
        VendedorDAO daoV = new VendedorDAO();
        int quantPaginas = (int) Math.ceil((double) daoV.numProdutosVendedor(vendedor) / 4);
        int paginaAtual = 1;
        while (true) {
            List<ItemCompra> vendas = dao.getVendas(vendedor, (paginaAtual - 1) * 4, 4);
            if (vendas.isEmpty()) {
                throw new OperacaoVendaException("nenhum Produto vendido");
            }
            if (paginaAtual == 1) {
                System.out.println("\n-------------- Vendas "+vendedor.getNomeLoja()+" -------------------");
            }
            System.out.println("\n\n          Pagina: " + paginaAtual + "/" + quantPaginas);

            long idProd = (long) -1;
            for (ItemCompra item : vendas) {
                if(idProd != -1L && Objects.equals(item.getProduto().getId_prod(), idProd)){
                    continue;
                }

                System.out.println("=".repeat(40));
                System.out.print("Nome do Produto: " + item.getProduto().getNome());
                System.out.println("        Código: " + df.format(item.getProduto().getId_prod()));
                System.out.println("Quantidade vendida: " + item.getProduto().getVendas());
                System.out.println("Valor das vendas: " + dao.totalVendas(item.getProduto()));
                System.out.println("=".repeat(40));

                idProd = item.getProduto().getId_prod();
            }

            System.out.println("\n");

            int op = Main.validaInput(ler, new String[] {"[1] Voltar", "[2] Avançar", "[3] Vendas de um produto", "[4] Sair"});



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
            } else if (op == 3) {
                try {
                    System.out.print("Qual o codigo do produto:");
                    Long prod = Long.parseLong(ler.nextLine());

                    mostrarItens(prod, ler);
                } catch (OperacaoVendaException e){
                    System.out.println("erro: "+e.getMessage());
                }
                catch (NumberFormatException e) {
                    System.out.println("Erro: digite um número válido.");
                }

            } else if(op == 4){
                break;
            }
            else {
                System.out.println("Opção inválida ou indisponível.");
            }

        }
    }
    private void mostrarItens(Long idProd, Scanner ler){
        CompraDAO dao = new CompraDAO();
        ProdutoDAO daoP = new ProdutoDAO();

        Long idVendedor = (long) -1;
        int quantPaginas = (int) Math.ceil((double) dao.numItensProdutos(idProd) / 4);
        int paginaAtual = 1;
        while (true) {
            List<ItemCompra> vendas = dao.getVendasDeProduto(idProd, (paginaAtual - 1) * 4, 4);
            if (vendas.isEmpty()) {
                throw new OperacaoVendaException("Produto não encontrado");
            }
            if (paginaAtual == 1) {
                System.out.println("\n-------------- Vendas "+daoP.buscaProId(idProd).getNome()+" -------------------");
            }
            System.out.println("\n\n          Pagina: " + paginaAtual + "/" + quantPaginas);

            for (ItemCompra item : vendas) {

                System.out.println("=".repeat(40));
                System.out.print("Estado: ");
                String s = item.getProduto().isAtivo() ? "Ativo" : "Desativado";
                System.out.println(s);
                System.out.println("Nome do Produto: " + item.getProduto().getNome());
                System.out.print("Nome do Produto na Venda: " + item.getNomeProdAtual());
                System.out.println("        Código: " + df.format(item.getProduto().getId_prod()));
                System.out.println("Quantidade: " + item.getQuantidade());
                System.out.println("Valor Vendido: "+item.getValorAtual());
                System.out.println("Total: " + item.getSubTotal());
                System.out.println("\nData: "+item.getCompra().getHorario().getDayOfMonth()+"/"+
                        item.getCompra().getHorario().getMonthValue()+"/"+
                        item.getCompra().getHorario().getYear()+"  "+
                        item.getCompra().getHorario().getHour()+":"+
                        item.getCompra().getHorario().getMinute()+":"+
                        item.getCompra().getHorario().getSecond());
                System.out.println("=".repeat(40));

            }

            System.out.println("\n");

            int op = Main.validaInput(ler, new String[] {"[1] Voltar ", "[2] Avançar", "[3] Sair"});


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
            } else if (op == 3) {
               break;

            }
            else {
                System.out.println("Opção inválida ou indisponível.");
            }

        }

    }

    public void dashProdutos(Vendedor vendedor, Scanner ler){
        VendedorDAO dao = new VendedorDAO();
        VendedorService vs = new VendedorService();

        long cod = 0L;

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
                System.out.println("Preço: "+produto.getValorUnitario());
                System.out.println("=".repeat(40));

            }
            System.out.println("\n");
            int op = Main.validaInput(ler, new String[] {"[1] Voltar", "[2] Avançar", "[3] Alterar produto", "[4] Excluir produto", "[5] Sair"});

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
                while (cod == 0){
                    System.out.print("Digite o codigo para alterar:");
                    try {
                        cod = Long.parseLong(ler.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: digite um valor válido");
                        continue;
                    }
                    try {
                        vs.atualizarProduto(vendedor.getId(), cod, ler);
                        break;
                    } catch (ProdutoInvalidoException | VendedorNuloExcception | OperacaoVendaException e) {
                        System.out.println(e.getMessage());
                        cod = 0;
                    }
                }

            }
            else if(op == 4){
                while (cod == 0){

                    System.out.print("Digite o codigo para excluir:");
                    try {
                        cod = Long.parseLong(ler.nextLine().trim());
                    } catch (InputMismatchException e) {
                        System.out.println("Erro: digite um valor válido");
                        continue;
                    }
                    try {
                        vs.excluirProduto(vendedor.getId(), cod);
                        break;
                    } catch (ProdutoInvalidoException | VendedorNuloExcception | OperacaoVendaException e) {
                        System.out.println(e.getMessage());
                        cod = 0;
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
}
