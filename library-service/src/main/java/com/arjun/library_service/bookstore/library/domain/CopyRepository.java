package com.arjun.library_service.bookstore.library.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopyRepository extends JpaRepository<CopyEntity, String> {

    Optional<CopyEntity> findByBarcode(String barcode);

    List<CopyEntity> findByProductCode(String productCode);

    List<CopyEntity> findByProductCodeAndStatus(String productCode, CopyStatus status);

    List<CopyEntity> findByStatus(CopyStatus status);
}
