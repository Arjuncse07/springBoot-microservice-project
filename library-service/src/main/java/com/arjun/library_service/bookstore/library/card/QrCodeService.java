package com.arjun.library_service.bookstore.library.card;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QrCodeService {

    private final LogoImageLoader logoImageLoader;

    @Value("${library.qr.logo-size-ratio:0.20}")
    private double logoSizeRatio;

    public QrCodeService(LogoImageLoader logoImageLoader) {
        this.logoImageLoader = logoImageLoader;
    }

    public String buildPayload(String cardId, Long userId, Long organizationId) {
        return "{\"cardId\":\"" + cardId + "\",\"userId\":" + userId + ",\"orgId\":" + organizationId + "}";
    }

    public String generateBase64Png(String payload, String organizationLogoUrl) {
        BufferedImage qrImage = renderQrMatrix(payload);
        Optional<BufferedImage> logo = logoImageLoader.load(organizationLogoUrl);
        BufferedImage finalImage = logo.map(img -> overlayLogo(qrImage, img)).orElse(qrImage);
        return toBase64Png(finalImage);
    }

    private BufferedImage renderQrMatrix(String payload) {
        try {
            Map<EncodeHintType, Object> hints = Map.of(
                    EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H,
                    EncodeHintType.MARGIN, 1,
                    EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix matrix = new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, 300, 300, hints);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate QR code", ex);
        }
    }

    private BufferedImage overlayLogo(BufferedImage qrImage, BufferedImage logo) {
        int qrSize = qrImage.getWidth();
        int logoSize = (int) (qrSize * logoSizeRatio);
        Image scaledLogo = logo.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);

        int pad = 4;
        BufferedImage combined = new BufferedImage(qrSize, qrSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(qrImage, 0, 0, null);

        int x = (qrSize - logoSize) / 2;
        int y = (qrSize - logoSize) / 2;
        g.setColor(Color.WHITE);
        g.fillRoundRect(x - pad, y - pad, logoSize + pad * 2, logoSize + pad * 2, 8, 8);
        g.drawImage(scaledLogo, x, y, null);
        g.dispose();
        return combined;
    }

    private String toBase64Png(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to encode QR PNG", ex);
        }
    }
}
