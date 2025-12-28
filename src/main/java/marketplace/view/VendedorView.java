package marketplace.view;

import marketplace.model.Produto;

import java.util.Scanner;

public class VendedorView {

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
            //todo add new exception
            throw new RuntimeException(e);
        }

    }
}
