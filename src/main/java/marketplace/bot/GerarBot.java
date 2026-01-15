package marketplace.bot;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import marketplace.model.Cliente;
import marketplace.model.Vendedor;
import marketplace.service.BotService;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GerarBot {


    private final BotService  bot = new BotService();

    private final Queue<Cliente> bufferClientes = new LinkedList<>();
    private final Queue<Vendedor> bufferVendedor = new LinkedList<>();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString());
                }
            })
            .create();

    public Cliente criarCliente() {
        if (bufferClientes.isEmpty()) {
            recarregarCliente();
        }

        return bufferClientes.poll();
    }

    public void recarregarCliente(){
        String prompt = "Gere uma lista com 5 perfis de clientes brasileiros fictícios para marketplace. " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
                "[ { \"nome\": \"String\", \"email\": \"String\", \"senha\": \"String\" }, ... ]";


        try {
            String json = bot.enviarPrompt(prompt);

            Type listaClienteType = new TypeToken<List<Cliente>>(){}.getType();
            List<Cliente> novosClientes = gson.fromJson(json, listaClienteType);

            if (novosClientes != null) {
                bufferClientes.addAll(novosClientes);
            }
            bufferClientes.forEach( c -> c.setBot(true));
            
        } catch (Exception e) {
            System.out.println();
        }
    }
    
    public Vendedor criarVendedor(){
        if(bufferVendedor.isEmpty()){
            recarregarVendedor();
        }
        
        return bufferVendedor.poll();
    }

    private void recarregarVendedor() {

        String prompt = "Gere uma lista com 5 perfis de vendedores (lojas) fictícios para marketplace. " +
                "Varie os nichos (eletrônicos, roupas, móveis, etc). " +
                "Responda APENAS com um JSON Array válido (sem markdown), neste formato: " +
                "[ { \"nomeLoja\": \"String\", \"cnpj\": \"String\", " +
                "\"email\": \"String\", \"senha\": \"String\", \"nicho\": \"String\" }, ... ]";

        try {
            String json = bot.enviarPrompt(prompt);

            Type listaVendedorType = new TypeToken<List<Cliente>>(){}.getType();
            List<Vendedor> novosVendedores = gson.fromJson(json, listaVendedorType);

            if (novosVendedores != null) {
                bufferVendedor.addAll(novosVendedores);
            }
            bufferVendedor.forEach( v -> v.setBot(true));

        } catch (Exception e) {
            System.out.println();
        }

    }



}
