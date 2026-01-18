package marketplace.bot;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import marketplace.dao.ClientesDAO;
import marketplace.dao.CompraDAO;
import marketplace.dao.ProdutoDAO;
import marketplace.dao.VendedorDAO;
import marketplace.exceptions.ProdutoInvalidoException;
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
import java.util.*;

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
        String prompt = "Gere uma lista com 5 perfis de clientes brasileiros fictícios para marketplace. " +
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
            System.out.println(e.getMessage());
        }
    }
    


    public void criarVendedor() {

        String prompt = "Gere uma lista com 5 perfis de vendedores (lojas) fictícios para marketplace. " +
                "Varie os nichos (eletrônicos, roupas, móveis, etc). " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
                "[ { \"nomeLoja\": \"String\", \"cnpj\": \"String\", " +
                "\"email\": \"String\", \"senha\": \"String\", \"nicho\": \"String\" }, ... ]";



        try {
            String json = bot.enviarPrompt(prompt);

            VendedorService service = new VendedorService();

            Type listaVendedorType = new TypeToken<List<Vendedor>>(){}.getType();
            List<Vendedor> novosVendedores = gson.fromJson(json, listaVendedorType);

            if (novosVendedores != null) {
                novosVendedores.forEach(v -> v.setBot(true));
                for (Vendedor vendedor : novosVendedores){
                    service.criarVendedorBOT(vendedor);
                }
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    }

    public void criarProduto(){
        VendedorService service = new VendedorService();
        VendedorDAO dao = new VendedorDAO();

        List<Vendedor> vendedores = dao.vendedoresBOT();
        if(vendedores.isEmpty()){
            return;
        }
        Vendedor vendedor;

        Random rand = new Random();
        do {
            int valor = rand.nextInt(vendedores.size());
            vendedor = vendedores.get(valor);
        } while (!vendedor.getEstoque().isEmpty());


        String prompt = "Gere uma lista de 3 produtos simples aleatórios de  R$: 250  ou menos para um loja fictícia chamada "+vendedor.getNomeLoja()+" em um marketplace. " +
                "Varie os objetos de acordo com o nicho: "+vendedor.getNicho()+". " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
               " [ { \"nome\": \"String\", \"valorUnitario\": \"Double\", \"quantidade\": \"int\" }, ... ]";

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

            System.out.println(e.getMessage());
        }

    }

    public void fazerCompra(int quantidade){
        ClienteService service = new ClienteService();
        ClientesDAO dao = new ClientesDAO();
        ProdutoDAO daoP = new ProdutoDAO();
        CompraDAO daoc = new CompraDAO();

        int quantCompra = 0;
        Produto produtoAtual;

        List<Cliente> clientes = dao.clientesBOT();

        if(clientes.isEmpty()){
            return;
        }
        Collections.shuffle(clientes);
        List<Cliente> clientesSelecionados = clientes.subList(0, Math.min(quantidade, clientes.size()));

        long totalProdutos = daoP.numProdutos();
        if (totalProdutos == 0) {
            return;
        }

        Random rand = new Random();



        try {
            long secaoAleLong = (totalProdutos > 10) ? rand.nextLong(totalProdutos - 5) : 0;


            List<Produto> secaoProdutos = daoP.listarProdutos((int) secaoAleLong, (int) secaoAleLong + 10);
            if (secaoProdutos == null || secaoProdutos.isEmpty()) {
                return;
            }


            for (Cliente cliente : clientesSelecionados) {

                Collections.shuffle(secaoProdutos);
                int tentativas = 0;

                for (Produto produto : secaoProdutos) {
                    produtoAtual = produto;

                    if (tentativas >= 4) {
                        break;
                    }

                    try {
                        if (produto.getQuantidade() > 0) {
                             quantCompra = rand.nextInt(Math.max(1, Math.min(3, produto.getQuantidade()))) + 1;

                            service.adicionarProduto(cliente.getId(), produto.getId_prod(), quantCompra);
                        }
                        var compraAtiva = dao.compraAtiva(cliente);
                        if (compraAtiva != null) {
                            var item = daoc.itemPeloProduto(compraAtiva, produto.getId_prod());
                            if (item != null) {
                                item.setBot(true);
                                daoc.merge(compraAtiva);
                            }
                        }
                        tentativas++;
                    } catch (Exception ignored) {
                        System.out.println("\nO produto: "+produtoAtual.getNome()+" de id: "+produto.getId_prod() + " para a quantidade "+quantCompra+" está indisponível");
                    }
                }
            }
        }
        catch (Exception ignored) {
        }

    }

    public void finalizarCompra(int quantidade){
        ClienteService service = new ClienteService();
        ClientesDAO dao = new ClientesDAO();

        List<Cliente> clientesAtivos = dao.clientesCompraAtivaBOT();

        if (clientesAtivos.isEmpty()) return;

        Collections.shuffle(clientesAtivos);

        int limite = Math.min(quantidade, clientesAtivos.size());

        for (int i = 0; i < limite; i++) {
            Cliente cliente = clientesAtivos.get(i);
            try {
                var compra = dao.compraAtiva(cliente);
                if (compra != null && compra.getItens() != null && !compra.getItens().isEmpty()) {
                    service.finalizarCompra(cliente.getId());
                }

                service.finalizarCompra(cliente.getId());
            } catch (Exception ignored) {
            }
        }


    }



}
