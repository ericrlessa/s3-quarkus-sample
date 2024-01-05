package ecomarkets.domain.core.product.image;

import ecomarkets.domain.ProductImage;
import ecomarkets.domain.ProductImageBuilder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.FileOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProductImageGetTest extends ProductImageTest{
    @Inject
    S3Client s3Client;
    ProductImage productImage;

    @BeforeEach
    @Transactional
    void startProductFixture(){
        productImage = ProductImageBuilder.newInstance()
                .withFileName(fileName)
                .withBucket(bucketName)
                .withMimeType(mimetype)
                .addTag("fileName", fileName).build();
        productImage.persist();

        imageRepository.save(file, productImage);
    }
    @Test
    public void testGetS3ProductImage() {
        byte [] file = given()
            .when()
            .get("/api/image/" + productImage.id)
            .then()
            .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asByteArray(); // Adjust the expected status code as needed

        assertThat(file, notNullValue());

        //saveFileLocalToVerifyManually(file);
    }

    @Test
    public void testPresignedGetUrlS3() {
        String preAssignedUrl = given()
                .when()
                .get("/api/image/%d/presignedGetUrl".formatted(productImage.id))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asString();

        byte [] file = given()
                .baseUri(preAssignedUrl)
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asByteArray();

        assertThat(file, notNullValue());

     //   saveFileLocalToVerifyManually(file);
    }

    private void saveFileLocalToVerifyManually(byte [] file){
        String localFilePath = "/tmp/test.jpg";

        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            fos.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
