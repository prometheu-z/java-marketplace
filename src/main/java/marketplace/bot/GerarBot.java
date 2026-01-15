package marketplace.bot;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.dao.ProdutoDAO;
import marketplace.dao.VendedorDAO;
import marketplace.model.Cliente;
import marketplace.model.Produto;
import marketplace.model.Vendedor;
import marketplace.service.BotService;
import marketplace.service.ClienteService;
import marketplace.service.VendedorService;
import marketplace.view.VendedorView;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GerarBot {


    private final BotService  bot = new BotService();


    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString());
                }
            })
            .create();



    public void criarCliente(){
        String prompt = "Gere uma lista com 10 perfis de clientes brasileiros fictícios para marketplace. " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
                "[ { \"nome\": \"String\", \"email\": \"String\", \"senha\": \"String\" }, ... ]";


        try {
            String json = bot.enviarPrompt(prompt);

            ClienteService service = new ClienteService();

            Type listaClienteType = new TypeToken<List<Cliente>>(){}.getType();
            List<Cliente> novosClientes = gson.fromJson(json, listaClienteType);


            if (novosClientes != null) {

                novosClientes.forEach(c -> c.setBot(true));
                for(Cliente cliente : novosClientes){
                    service.criarClienteBOT(cliente);
                }
            }
            
        } catch (Exception e) {
            System.out.println("criarcliente:"+e.getMessage());
        }
    }
    


    public void criarVendedor() {

        String prompt = "Gere uma lista com  perfis de vendedores (lojas) fictícios para marketplace. " +
                "Varie os nichos (eletrônicos, roupas, móveis, etc). " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
                "[ { \"nomeLoja\": \"String\", \"cnpj\": \"String\", " +
                "\"email\": \"String\", \"senha\": \"String\", \"nicho\": \"String\" }, ... ]";


        Vendedor vendedorC = null;

        try {
            String json = bot.enviarPrompt(prompt);

            VendedorService service = new VendedorService();

            Type listaVendedorType = new TypeToken<List<Vendedor>>(){}.getType();
            List<Vendedor> novosVendedores = gson.fromJson(json, listaVendedorType);

            if (novosVendedores != null) {
                novosVendedores.forEach(v -> v.setBot(true));
                for (Vendedor vendedor : novosVendedores){
                    vendedorC = vendedor;
                    service.criarVendedorBOT(vendedor);
                }
            }

        } catch (Exception e) {
            System.out.println("Erro criando: "+vendedorC.getId());
        }

    }

    public void criarProduto(){
        VendedorService service = new VendedorService();
        VendedorDAO dao = new VendedorDAO();

        List<Vendedor> vendedores = dao.vendedoresBOT();
        if(vendedores.isEmpty()){
            return;
        }

        Random rand = new Random();
        Vendedor vendedor = vendedores.get(rand.nextInt(vendedores.size()));


        String prompt = "Gere uma lista entre 1 ou 10 produtos para um loja fictícia chamada "+vendedor.getNomeLoja()+" em um marketplace. " +
                "Varie os objetos de acordo com o nicho: "+vendedor.getNicho()+". " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
                "\"nome\": \"String\", \"valorUnitario\": \"Double\", \"quantidade\": \"int\" }, ... ]";

        try {
            String json = bot.enviarPrompt(prompt);


            Type listaProdutosType = new TypeToken<List<Produto>>(){}.getType();
            List<Produto> novosProdutos = gson.fromJson(json, listaProdutosType);

            if (novosProdutos != null) {
                novosProdutos.forEach( p -> p.setBot(true));

                for(Produto produto : novosProdutos){
                    service.criarProdutoBOT(vendedor.getId(), produto);
                }
            }


        } catch (Exception e) {
            System.out.println("Erro criando produto de: "+vendedor.getId());
        }

    }

    public void fazerCompra(){
        ClienteService service = new ClienteService();
        ClientesDAO dao = new ClientesDAO();
        ProdutoDAO daoP = new ProdutoDAO();
        CompraDAO daoc = new CompraDAO();

        List<Cliente> clientes = dao.clientesBOT();

        if(clientes.isEmpty()){
            return;
        }
        Random rand = new Random();

        Cliente cliente = clientes.get(rand.nextInt(clientes.size()));

        long totalProdutos = daoP.numProdutos();


        if (totalProdutos == 0) {
            return;
        }

        long secaoAleLong = rand.nextLong(totalProdutos);
        int secaoAle = (int) secaoAleLong;

        try {

            List<Produto> novosProdutos = daoP.listarProdutos(secaoAle, secaoAle+3);

            if (novosProdutos != null) {

                for(Produto produto : novosProdutos){
                    int quant = rand.nextInt(produto.getQuantidade()/2);
                    service.adicionarProduto(cliente.getId(), produto.getId_prod(), quant);


                    daoc.itemPeloProduto(dao.compraAtiva(cliente), produto.getId_prod()).setBot(true);
                }
            }
            cliente.getCompras().forEach(c -> c.setBot(true));

            dao.merge(cliente);

        } catch (Exception e) {
            System.out.println("Erro fazendo compra de cliente: "+cliente.getId());
        }

    }

    public void finalizarCompra(){
        ClienteService service = new ClienteService();
        ClientesDAO dao = new ClientesDAO();

        List<Cliente> clientes = dao.clientesCompraAtivaBOT();

        for (Cliente c : clientes){
            System.out.println("Clientes compra ativa:"+c.getId());
        }

        if(clientes.isEmpty()){
            return;
        }

        Random rand = new Random();

        Cliente cliente = clientes.get(rand.nextInt(clientes.size()));



        try {

            service.finalizarCompra(cliente);
        } catch (Exception e) {
            System.out.println("Erro finalizando compra de cliente: "+cliente.getId());
        }

    }



}
