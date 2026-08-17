package com.arjun.ai_service.bookstore.web.controllers;

import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/smoke")
@Validated
public class AiSmokeController {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;


    public AiSmokeController(ChatClient chatClient,
                             EmbeddingModel embeddingModel,
                             VectorStore vectorStore)
    {
        this.chatClient = chatClient;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    public record SmokeRequest(@NotBlank String text){}

    @PostMapping("/chat")
    public Map<String,String> chat(@RequestBody @Validated SmokeRequest request){
      String answer = chatClient.prompt()
                                .user(request.text())
                                 .call().content();
      return Map.of("answer", answer == null ? "" : answer);
    }

    @PostMapping("/embed")
    public Map<String,Object> embed(@RequestBody @Validated SmokeRequest request){
        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(request.text()));
        int dimensions = embeddingResponse.getResult().getOutput().length;
        return Map.of("dimensions", dimensions,
                "vectorStore",vectorStore.getClass().getSimpleName());
    }




}
