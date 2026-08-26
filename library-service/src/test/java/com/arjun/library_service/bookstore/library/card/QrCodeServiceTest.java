package com.arjun.library_service.bookstore.library.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceTest {

    @Mock
    private LogoImageLoader logoImageLoader;

    @InjectMocks
    private QrCodeService qrCodeService;

    @Test
    void shouldGenerateBase64PngWithoutLogo() {
        when(logoImageLoader.load(null)).thenReturn(java.util.Optional.empty());

        String payload = "{\"cardId\":\"CARD-2026-0001\",\"userId\":1,\"orgId\":1}";
        String base64 = qrCodeService.generateBase64Png(payload, null);

        assertThat(base64).isNotBlank();
    }

    @Test
    void shouldBuildPayload() {
        String payload = qrCodeService.buildPayload("CARD-2026-0001", 1L, 1L);
        assertThat(payload).contains("CARD-2026-0001");
        assertThat(payload).contains("\"userId\":1");
    }
}
