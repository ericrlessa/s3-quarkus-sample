package s3sample.domain.core.product.image;

import s3sample.domain.ImageDataRepository;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ImageTest {
    String fileName = "Vitoria_ES_Brasil.jpg";
    String mimetype = "image/jpeg";
    Path file = Paths.get("src/test/resources/s3sample/domain/core/product/" + fileName);
    @ConfigProperty(name = "bucket.name")
    String bucketName;
    @Inject
    ImageDataRepository imageDataRepository;
}
