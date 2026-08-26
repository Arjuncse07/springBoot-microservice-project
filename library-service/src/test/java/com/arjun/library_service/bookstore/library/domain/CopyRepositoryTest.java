package com.arjun.library_service.bookstore.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(com.arjun.library_service.TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CopyRepositoryTest {

    @Autowired
    private CopyRepository copyRepository;

    @Test
    void shouldFindAllSeedCopies() {
        List<CopyEntity> copies = copyRepository.findAll();
        assertThat(copies).hasSize(5);
    }

    @Test
    void shouldFindCopyByBarcode() {
        CopyEntity copy = copyRepository.findByBarcode("BC-P100").orElseThrow();
        assertThat(copy.getProductCode()).isEqualTo("P100");
        assertThat(copy.getStatus()).isEqualTo(CopyStatus.AVAILABLE);
        assertThat(copy.getOrganizationId()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmptyWhenBarcodeNotFound() {
        assertThat(copyRepository.findByBarcode("BC-MISSING")).isEmpty();
    }

    @Test
    void shouldFilterByProductCodeAndStatus() {
        List<CopyEntity> copies = copyRepository.findByProductCodeAndStatus("P102", CopyStatus.AVAILABLE);
        assertThat(copies).hasSize(1);
        assertThat(copies.getFirst().getBarcode()).isEqualTo("BC-P102");
    }
}
