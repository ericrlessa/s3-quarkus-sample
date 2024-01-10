package ecomarkets.domain;

import java.nio.file.Path;

public interface ImageDataRepository {

    public void save(Path file,
                     Image image);
    public byte [] find(Image image);

    public void delete(Image image);

    public String createPresignedGetUrl(Image image);

    public String getBucketName();

}
