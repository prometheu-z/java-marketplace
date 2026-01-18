package marketplace.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.gson.*;
import marketplace.model.Vendedor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Properties;

public class BotService {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";


    private String getApi_key()  {
        // pegar chave de api de config.properties na raiz do programa
        try {
            Properties prop = new Properties();
            FileInputStream input = new FileInputStream("config.properties");
            prop.load(input);

            return prop.getProperty("gemini.api.key");
        } catch (IOException e) {
            System.out.println("erro:"+e.getMessage());
            return null;
        }
    }

    public String enviarPrompt(String prompt){

        try {
            String apiKey = getApi_key();
            if (apiKey == null) {
                return null;
            }

            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", prompt);

            JsonArray parts = new JsonArray();
            parts.add(textPart);

            JsonObject content = new JsonObject();
            content.add("parts", parts);

            JsonArray contents = new JsonArray();
            contents.add(content);

            JsonObject bodyJson = new JsonObject();
            bodyJson.add("contents", contents);

            String requestBody = new Gson().toJson(bodyJson);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return extrair(response.body());
            }
            else {
                throw new Exception("A cota diária de sua api expirou, crie uma nova para um novo projeto, pelo link: https://aistudio.google.com");
            }
        } catch (IOException | InterruptedException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String extrair(String json) {

        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                JsonObject content = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                String texto = parts.get(0).getAsJsonObject().get("text").getAsString();

                return limpar(texto);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Erro:"+e.getMessage());

        }
        return null;

    }

    private String limpar(String texto) {
        if (texto == null) return null;
        if (texto.contains("```json")) {
            return texto.split("```json")[1].split("```")[0].trim();
        } else if (texto.contains("```")) {
            return texto.split("```")[1].split("```")[0].trim();
        }
        return texto.trim();
    }


}

