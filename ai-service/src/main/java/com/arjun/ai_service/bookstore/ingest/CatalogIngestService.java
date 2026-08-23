package com.arjun.ai_service.bookstore.ingest;

import com.arjun.ai_service.bookstore.catalog.CatalogClient;
import com.arjun.ai_service.bookstore.catalog.CatalogProduct;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CatalogIngestService {

    private static final Logger log = (Logger) LoggerFactory.getLogger(String.valueOf(CatalogIngestService.class));

    private final CatalogClient catalogClient;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public CatalogIngestService(
            CatalogClient catalogClient, VectorStore vectorStore, JdbcTemplate jdbcTemplate){
        this.catalogClient = catalogClient;
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    public int reindex(){
        List<CatalogProduct> products = catalogClient.fetchAllProducts();
        clearVectorStore();
        List<Document> documents = products.stream().map(this::toDocument).toList();
        if(!documents.isEmpty()){
            vectorStore.add(documents);
        }
        log.info("Re-indexed {} catalog products into vector store "+String.valueOf(documents.size())+" ");
        return documents.size();

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
