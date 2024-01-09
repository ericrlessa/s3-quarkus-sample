package ecomarkets.domain.core.product.image;

import ecomarkets.domain.ImageDataRepository;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ProductImageTest {
    String fileName = "Vitoria_ES_Brasil.jpg";
    String mimetype = "image/jpeg";
    Path file = Paths.get("src/test/resources/ecomarkets/domain/core/product/" + fileName);
    @ConfigProperty(name = "bucket.name")
    String bucketName;
    @Inject
    ImageDataRepository imageDataRepository;
}
