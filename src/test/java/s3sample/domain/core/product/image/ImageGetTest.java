package s3sample.domain.core.product.image;

import s3sample.domain.Image;
import s3sample.domain.ImageBuilder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ImageGetTest extends ImageTest {
    Image productImage;

    @BeforeEach
    @Transactional
    void startProductFixture(){
        productImage = ImageBuilder.newInstance()
                .withFileName(fileName)
                .withBucket(bucketName)
                .withMimeType(mimetype)
                .addTag("fileName", fileName)
                .addTag("city", "Vitoria")
                .addTag("country", "Brasil")
                .build();
        productImage.persist();

        imageDataRepository.save(file, productImage);
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
