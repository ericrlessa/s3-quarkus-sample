package ecomarkets.domain;

import java.nio.file.Path;

public interface ImageDataRepository {

    public void save(Path file,
                     Image productImage);
    public byte [] find(Image productImage);

    public void delete(Image productImage);

    public String createPresignedGetUrl(Image productImage);

    public String getBucketName();

}
