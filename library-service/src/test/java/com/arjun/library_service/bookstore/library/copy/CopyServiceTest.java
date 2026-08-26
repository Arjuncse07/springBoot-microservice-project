package com.arjun.library_service.bookstore.library.copy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arjun.library_service.bookstore.library.catalog.CatalogClient;
import com.arjun.library_service.bookstore.library.catalog.CatalogProduct;
import com.arjun.library_service.bookstore.library.copy.dto.CopyResponse;
import com.arjun.library_service.bookstore.library.copy.dto.CreateCopyRequest;
import com.arjun.library_service.bookstore.library.domain.CopyEntity;
import com.arjun.library_service.bookstore.library.domain.CopyRepository;
import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import com.arjun.library_service.bookstore.library.domain.OrganizationRepository;
import com.arjun.library_service.bookstore.library.exception.DuplicateBarcodeException;
import com.arjun.library_service.bookstore.library.exception.OrganizationNotFoundException;
import com.arjun.library_service.bookstore.library.exception.ProductNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CopyServiceTest {

    @Mock
    private CopyRepository copyRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private CopyService copyService;

    @Test
    void shouldCreateCopyWhenValid() {
        CreateCopyRequest request = new CreateCopyRequest("BC-P105", "P105", 1L, "Shelf A", null);
        when(copyRepository.existsById("BC-P105")).thenReturn(false);
        when(organizationRepository.existsById(1L)).thenReturn(true);
        when(catalogClient.getProductByCode("P105"))
                .thenReturn(new CatalogProduct("P105", "The Giving Tree", null, null, new BigDecimal("32.0")));
        when(copyRepository.save(any(CopyEntity.class))).thenAnswer(invocation -> {
            CopyEntity copy = invocation.getArgument(0);
            return copy;
        });

        CopyResponse response = copyService.createCopy(request);

        assertThat(response.barcode()).isEqualTo("BC-P105");
        assertThat(response.productCode()).isEqualTo("P105");
        assertThat(response.status()).isEqualTo(CopyStatus.AVAILABLE);
    }

    @Test
    void shouldRejectDuplicateBarcode() {
        when(copyRepository.existsById("BC-P100")).thenReturn(true);

        assertThatThrownBy(() -> copyService.createCopy(new CreateCopyRequest("BC-P100", "P100", 1L, null, null)))
                .isInstanceOf(DuplicateBarcodeException.class);

        verify(catalogClient, never()).getProductByCode(any());
    }

    @Test
    void shouldRejectInvalidOrganization() {
        when(copyRepository.existsById("BC-P105")).thenReturn(false);
        when(organizationRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> copyService.createCopy(new CreateCopyRequest("BC-P105", "P105", 99L, null, null)))
                .isInstanceOf(OrganizationNotFoundException.class);
    }

    @Test
    void shouldRejectInvalidProductCode() {
        when(copyRepository.existsById("BC-P105")).thenReturn(false);
        when(organizationRepository.existsById(1L)).thenReturn(true);
        when(catalogClient.getProductByCode("INVALID")).thenThrow(ProductNotFoundException.forCode("INVALID"));

        assertThatThrownBy(() -> copyService.createCopy(new CreateCopyRequest("BC-P105", "INVALID", 1L, null, null)))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
