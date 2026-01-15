package marketplace.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import marketplace.model.Vendedor;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class BotService {

    private String getApi_key()  {
        try {
            Properties prop = new Properties();

            prop.load(BotService.class.getResourceAsStream("/config.properties"));

            return prop.getProperty("GEMINI_API_KEY");
        } catch (IOException e) {
            System.out.println("erro:"+e.getMessage());
            return null;
        }
    }

    public String enviarPrompt(String prompt){

        Client client = Client.builder()
                .apiKey(getApi_key())
                .build();

        GenerateContentResponse response = client.models.generateContent(
                "gemini-3-flash-preview",
                prompt,
                null
        );



        return extrair(Objects.requireNonNull(response.text()));
    }

    private String extrair(String texto) {
        if (texto == null) return null;

        if (texto.contains("```json")) {
            texto = texto.split("```json")[1].split("```")[0];
        } else if (texto.contains("```")) {
            texto = texto.split("```")[1].split("```")[0];
        }

        return texto.trim();
    }


}

