package com.arjun.ai_service.bookstore.ask;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookAskService {

    private static final int TOP_K = 4;
    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private String userPrompt;

    public BookAskService(VectorStore vectorStore, ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }


    public BookAskResponse ask(String question) {
        List<Document> matches = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(TOP_K).build());

        if (matches.isEmpty()) {
            return new BookAskResponse(
                    "I don't have any matching book in the catalog for that question.", List.of());
        }


        String context = matches.stream()
                .map(doc -> " - " + doc.getMetadata().get("code") + ": " + doc.getText())
                .collect(Collectors.joining("\n"));

        String userPrompt =
                """
                        Context from catalog
                              %s
                        
                              Question: %s
                        """.formatted(context, question);

        String answer = chatClient.prompt().user(userPrompt).call().content();
        return new BookAskResponse(answer == null ? "" : answer, extractCodes(matches));

    }

    public List<String> extractCodes(List<Document> documents) {
        Set<String> codes = new LinkedHashSet<>();
        for (Document doc : documents) {
            Object code = doc.getMetadata().get("code");
            if (code != null) {
                codes.add(code.toString());
            }
        }
        return new ArrayList<>(codes);
    }

}

/* Version
2026.01 : BookAskService.java created for Retrieve (vector search) → Augment (prompt + context) → Generate (Ollama chat)
Not “send whole catalog to LLM.”


 */