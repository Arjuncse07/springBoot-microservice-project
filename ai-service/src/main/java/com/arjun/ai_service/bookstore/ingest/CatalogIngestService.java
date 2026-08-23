package com.arjun.ai_service.bookstore.ingest;

import com.arjun.ai_service.bookstore.catalog.CatalogClient;
import com.arjun.ai_service.bookstore.catalog.CatalogProduct;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CatalogIngestService {

    private static final Logger log = LoggerFactory.getLogger(CatalogIngestService.class);

    private final CatalogClient catalogClient;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;
    private final int pageSize;

    public CatalogIngestService(
            CatalogClient catalogClient,
            VectorStore vectorStore,
            JdbcTemplate jdbcTemplate,
            @Value("${catalog.ingest.page-size:100}") int pageSize){
        this.catalogClient = catalogClient;
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
        this.pageSize = pageSize;
    }

    public int reindex(IngestScope ingestScope){
       clearVectorStore();
        AtomicInteger indexed = new AtomicInteger(0);

        catalogClient.forEachProductPage(pageSize, batch -> {
            List<Document> documents = batch.stream().map(this::toDocument).toList();
            if(!documents.isEmpty()){
                vectorStore.add(documents);
                indexed.addAndGet(documents.size());
            }
        });

        log.info("Reindexed {} catalog products into vector store (scope = {}) "+indexed.get()+ ingestScope);
        return indexed.get();
    }

    public int reindex(){
        return reindex(IngestScope.all());
    }

    public long vectorCount(){
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vector_store", Long.class);
        return count ==null ? 0L : count;
    }

    public boolean isEmpty(){
        return vectorCount() == 0L;
    }

    private void clearVectorStore() {
        jdbcTemplate.update("DELETE FROM vector_store");
    }


    private Document toDocument(CatalogProduct product) {
        String content = product.name() + "\n" + (product.description() == null ? "" : product.description());
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("code", product.code());
        metadata.put("name", product.name());
        if (product.price() != null) {
            metadata.put("price", product.price().doubleValue());
        }
        if (product.imageUrl() != null) {
            metadata.put("imageUrl", product.imageUrl());
        }
        return new Document(product.code(), content, metadata);
    }


}
