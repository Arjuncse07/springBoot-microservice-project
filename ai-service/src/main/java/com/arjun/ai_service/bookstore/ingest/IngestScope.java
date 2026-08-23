package com.arjun.ai_service.bookstore.ingest;


import java.util.Optional;

public record IngestScope(Optional<String> category) {

    public static IngestScope all(){
        return new IngestScope(Optional.empty());
    }

}
