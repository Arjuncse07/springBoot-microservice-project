package com.arjun.ai_service.bookstore.web.controllers;

import com.arjun.ai_service.bookstore.ingest.CatalogIngestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
public class BookIngestController {


    private final CatalogIngestService ingestService;

    public BookIngestController (CatalogIngestService ingestService){
        this.ingestService = ingestService;
    }

    @PostMapping("/reindex")
    public Map<String,Object> reindex(){
        int indexed = ingestService.reindex();
        return Map.of(
                "indexed", indexed,
                "vectorCount", ingestService.vectorCount()
        );

    }


}
