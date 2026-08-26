package com.arjun.library_service.bookstore.library.copy;

import com.arjun.library_service.bookstore.library.catalog.CatalogClient;
import com.arjun.library_service.bookstore.library.copy.dto.CopyResponse;
import com.arjun.library_service.bookstore.library.copy.dto.CreateCopyRequest;
import com.arjun.library_service.bookstore.library.domain.CopyEntity;
import com.arjun.library_service.bookstore.library.domain.CopyRepository;
import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import com.arjun.library_service.bookstore.library.domain.OrganizationRepository;
import com.arjun.library_service.bookstore.library.exception.CopyNotFoundException;
import com.arjun.library_service.bookstore.library.exception.DuplicateBarcodeException;
import com.arjun.library_service.bookstore.library.exception.OrganizationNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CopyService {

    private final CopyRepository copyRepository;
    private final OrganizationRepository organizationRepository;
    private final CatalogClient catalogClient;

    public CopyService(
            CopyRepository copyRepository,
            OrganizationRepository organizationRepository,
            CatalogClient catalogClient) {
        this.copyRepository = copyRepository;
        this.organizationRepository = organizationRepository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public CopyResponse createCopy(CreateCopyRequest request) {
        if (copyRepository.existsById(request.barcode())) {
            throw DuplicateBarcodeException.forBarcode(request.barcode());
        }
        if (!organizationRepository.existsById(request.organizationId())) {
            throw OrganizationNotFoundException.forId(request.organizationId());
        }
        catalogClient.getProductByCode(request.productCode());

        CopyEntity copy = new CopyEntity();
        copy.setBarcode(request.barcode());
        copy.setProductCode(request.productCode());
        copy.setOrganizationId(request.organizationId());
        copy.setLocation(request.location());
        copy.setConditionNotes(request.conditionNotes());
        copy.setStatus(CopyStatus.AVAILABLE);

        return CopyResponse.from(copyRepository.save(copy));
    }

    public CopyResponse getByBarcode(String barcode) {
        return copyRepository
                .findByBarcode(barcode)
                .map(CopyResponse::from)
                .orElseThrow(() -> CopyNotFoundException.forBarcode(barcode));
    }

    public List<CopyResponse> list(String productCode, CopyStatus status) {
        List<CopyEntity> copies;
        if (productCode != null && status != null) {
            copies = copyRepository.findByProductCodeAndStatus(productCode, status);
        } else if (productCode != null) {
            copies = copyRepository.findByProductCode(productCode);
        } else if (status != null) {
            copies = copyRepository.findByStatus(status);
        } else {
            copies = copyRepository.findAll();
        }
        return copies.stream().map(CopyResponse::from).toList();
    }

    @Transactional
    public CopyResponse updateStatus(String barcode, CopyStatus status) {
        CopyEntity copy = copyRepository
                .findByBarcode(barcode)
                .orElseThrow(() -> CopyNotFoundException.forBarcode(barcode));
        copy.setStatus(status);
        return CopyResponse.from(copyRepository.save(copy));
    }
}
