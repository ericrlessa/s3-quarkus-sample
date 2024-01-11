package ecomarkets.domain.core.product.image;

import ecomarkets.domain.Image;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ImagePostTest extends ImageTest {
    public void testUpdateS3ProductImage() {
        String id = given()
            .multiPart("file", file)
            .multiPart("fileName", fileName)
            .multiPart("mimeType", mimetype)
            .when()
            .post("/api/image")
            .then()
            .statusCode(HttpStatus.SC_OK)
                .extract()
                .body().asString(); // Adjust the expected status code as needed

        Image img = Image.findById(Long.valueOf(id));
        assertThat(img, notNullValue());
        assertThat(img.bucket(), equalTo(bucketName));
        assertThat(img.id, notNullValue());
        assertThat(img.fileName(), equalTo(fileName));
        assertThat(img.mimeType(), equalTo(mimetype));

        byte [] imageFileData = imageDataRepository.find(img);
        assertThat(imageFileData, notNullValue());

        imageDataRepository.delete(img);
    }
}