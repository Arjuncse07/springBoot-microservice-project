package com.arjun.ai_service.bookstore.ingest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.stereotype.Component;



@Component
public class CatalogIngestRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogIngestRunner.class);

    private final CatalogIngestService ingestService;

    public CatalogIngestRunner(CatalogIngestService ingestService){
        this.ingestService = ingestService;
    }

    @Override
    public void run(ApplicationArguments applicationArguments){
        if(ingestService.isEmpty()){
            log.info("Vector store is Empty - ingesting catalog product");
            ingestService.reindex();
        }else {
            log.info("Vector store already has {} documents - skip startup ingest " + ingestService.vectorCount() + " ");
        }
    }

}
