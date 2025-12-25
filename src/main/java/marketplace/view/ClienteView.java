package marketplace.view;

import marketplace.dao.ClientesDAO;
import marketplace.model.Cliente;
import marketplace.model.Compra;
import marketplace.model.ItemCompra;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ClienteView {

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


            DecimalFormat df = new DecimalFormat("000");
            System.out.printf(df.format(item.getId_itemVenda())+"  "+df.format(item.getQuantidade())+"    "+
                    item.getProduto().getNome());
            for (int j = 0; j<25-item.getProduto().getNome().length(); j++){
                System.out.print(" ");
            }
            System.out.println(item.getSubTotal());
            idVendedor = item.getVendedor().getId();
        }

        System.out.println("\n----------------------------");



    }
    public void gerarNotaFiscal(Cliente cliente){
        ClientesDAO dao = new ClientesDAO();
        //TO DO: try catch CompraNullException
        Compra compra = dao.getUltimasCompras(cliente, 1).getFirst();

        System.out.println("----------------------------");
        System.out.println("JAVA MARKETPLACE\nRua tal s/n\n");
        System.out.println("cnpj: 89.455.000/003-00\nCOD:"+compra.getId_compra());
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

