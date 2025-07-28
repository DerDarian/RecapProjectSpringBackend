package org.example.recapprojectspring.Spellcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(RestClient.Builder restClientBuilder, @Value("${OPEN_AI_KEY}") String apiKey) {
        this.restClient = restClientBuilder.baseUrl("https://api.openai.com/v1/responses").defaultHeader("Authorization", "Bearer " + apiKey).build();
    }

    String performRequest(OpenAIRequest openAIRequest){
        OpenAIResponse response = Objects.requireNonNull(restClient.post().contentType(MediaType.APPLICATION_JSON).body(openAIRequest).retrieve().body(OpenAIResponse.class));
        System.out.println(response);
        return response.output().getFirst().content().getFirst().text();
    }

    public String correctSpelling(String text){
        String language = determineLanguage(text).toLowerCase();
        System.out.println("Language: " + language);
        System.out.println("Text: " + text);
        if(language.equals("german"))
            return performRequest(makeSpellCheckRequest_German(text));
        else
            return performRequest(makeSpellCheckRequest(text));
    }

    private String determineLanguage(String text){
        String model = "gpt-4o";
        String instructions = "Reply with one word only. Respond with the name of the language the text is in.";
        return performRequest(new  OpenAIRequest(model, instructions, text));
    }

    private OpenAIRequest makeSpellCheckRequest_German(String text){
        String model = "gpt-4o";
        String instructions = "Du sprichst deutsch und antwortest auf deutsch. Du gibst ausschließlich den mitgegeben Text mit korrigierter Rechtschreibung zurück.";
        return new  OpenAIRequest(model, instructions, text);
    }

    private OpenAIRequest makeSpellCheckRequest(String text){
        String model = "gpt-4o";
        String instructions = "You are only returning the provided text back to the user, but with corrected spelling.";
        return new  OpenAIRequest(model, instructions, text);
    }
}
