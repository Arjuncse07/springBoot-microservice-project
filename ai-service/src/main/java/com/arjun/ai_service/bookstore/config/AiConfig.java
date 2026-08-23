package com.arjun.ai_service.bookstore.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private static final String SYSTEM_PROMPT =
            """
            You are a bookstore catalog advisor. Answer ONLY using the provided context.
            Recommend books from the context and always mention their product codes (e.g. P105).
            If the answer is not in the context, say you do not know and do not invent book titles or codes.
            """;
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(SYSTEM_PROMPT).build();
    }

}
