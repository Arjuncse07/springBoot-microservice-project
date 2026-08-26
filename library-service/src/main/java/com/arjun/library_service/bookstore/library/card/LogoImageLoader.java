package com.arjun.library_service.bookstore.library.card;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogoImageLoader {

    private static final Logger log = LoggerFactory.getLogger(LogoImageLoader.class);

    public Optional<BufferedImage> load(String logoUrl) {
        if (logoUrl == null || logoUrl.isBlank()) {
            return Optional.empty();
        }
        try (InputStream in = URI.create(logoUrl).toURL().openStream()) {
            BufferedImage image = ImageIO.read(in);
            return Optional.ofNullable(image);
        } catch (Exception ex) {
            log.warn("Failed to load logo from {}: {}", logoUrl, ex.getMessage());
            return Optional.empty();
        }
    }
}
