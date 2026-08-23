package com.arjun.ai_service.bookstore.web.controllers;

import com.arjun.ai_service.bookstore.ingest.CatalogIngestService;
import com.arjun.ai_service.bookstore.ingest.IngestScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookIngestController {


    private final CatalogIngestService ingestService;

    public BookIngestController (CatalogIngestService ingestService){
        this.ingestService = ingestService;
    }

    public record ReindexRequest(Optional<String> category) {}

    @PostMapping("/reindex")
    public Map<String,Object> reindex(@RequestBody(required = false) ReindexRequest request){
        IngestScope ingestScope = request == null
                ? IngestScope.all()
                : new IngestScope(request.category());

        int indexed = ingestService.reindex(ingestScope);
        return Map.of(
                "indexed", indexed,
                "vectorCount", ingestService.vectorCount(),
                "scope", ingestScope.category().orElse("all")
        );

    }


}


/* Version :
2026.01 : indexed is the number of catalog products that were turned into vector documents and written into the vector store during that reindex call.
It’s the return value of CatalogIngestService.reindex() — a running count of documents added via vectorStore.add(...).
It’s not a status flag; it’s a count of how many products were ingested in that run.


 */