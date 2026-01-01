package marketplace.view;

import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.exceptions.EntradaInvalidaException;
import marketplace.model.Cliente;
import marketplace.model.Compra;
import marketplace.model.ItemCompra;
import marketplace.model.Produto;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ClienteView {

    private final DecimalFormat df = new DecimalFormat("000");

    public Cliente criarCliente(){

        try {
            Scanner ler = new Scanner(System.in);
            System.out.println("------------ Cadastro --------------");
            System.out.println("Qual o seu nome:");
            String nome = ler.nextLine();
            System.out.println("Qual o seu email:");
            String email = ler.nextLine();
            System.out.println("Crie uma senha:");
            String senha = ler.nextLine();
            System.out.println("\n  Usuário cadastrado!");

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
                    email.isEmpty() ? email : cliente.getEmail(),
                    senha.isEmpty() ? cliente.getSenha() : senha);

        } catch (Exception e) {
            throw new EntradaInvalidaException("Entrada de dados inválidos");
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
            if(paginaAtual != 1){
                System.out.print("[1] voltar      ");
            }
            if(paginaAtual != quantPaginas){
                System.out.print("[2] avançar");
            }
            System.out.println("\n[3] gerar cupom fiscal      [4] sair");
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
                System.out.println("Qual o codigo da compra:");
                Long idCupom = ler.nextLong();
                ler.nextLine();
                gerarNotaFiscal(idCupom);
            }
            else {
                break;
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
        //TO DO: try catch CompraNullException
        Compra compra = dao.buscarPorId(idCompra);

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

