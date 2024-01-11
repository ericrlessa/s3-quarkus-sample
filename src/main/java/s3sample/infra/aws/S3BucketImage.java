package s3sample.infra.aws;

import s3sample.domain.ImageDataRepository;
import s3sample.domain.Image;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class S3BucketImage implements ImageDataRepository {
    @ConfigProperty(name = "bucket.name")
    private String bucketName;
    @ConfigProperty(name = "presigned.url.duration.in.minutes")
    private Integer presignedUrlDurationInMinutes;
    @Inject
    private S3Client s3;
    @Inject
    private S3Presigner presigner;

    public String getBucketName(){
        return this.bucketName;
    }

    public void save(Path file,
                     Image productImage) {

        List<Tag> tagsS3 = getTags(productImage);
        s3.putObject(
        PutObjectRequest.builder()
                .bucket(productImage.bucket())
                .key(productImage.key())
                .contentType(productImage.mimeType())
                .tagging(Tagging.builder().tagSet(tagsS3).build())
                .build(),
                RequestBody.fromFile(file));
    }
    public void delete(Image productImage) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(productImage.bucket())
                .key(productImage.key())
                .build();

        s3.deleteObject(deleteRequest);
    }

    public byte[] find(Image productImage) {
        try {
            return s3.getObject(GetObjectRequest.builder()
                       .bucket(productImage.bucket())
                       .key(productImage.key())
                       .build()).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createPresignedGetUrl(Image productImage) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(productImage.bucket())
                .key(productImage.key())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlDurationInMinutes))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toExternalForm();
    }

    private List<Tag> getTags(Image productImage) {
        List<Tag> tagsS3 = productImage.tags().stream().map(
                t -> parseTagS3(t)
        ).collect(Collectors.toList());
        return tagsS3;
    }

    private Tag parseTagS3(s3sample.domain.Tag t) {
        return Tag.builder().key(t.key()).value(t.value()).build();
    }
    @PostConstruct

    private void createBucket() {
        try {
            try {
                HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build();
                s3.headBucket(headBucketRequest);
            } catch (NoSuchBucketException e) {
                CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build();
                s3.createBucket(bucketRequest);
            }
        } catch (Exception e) {
            //FIXME add log
        }
    }
}
