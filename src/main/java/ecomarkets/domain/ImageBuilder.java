package ecomarkets.domain;

import java.util.ArrayList;
import java.util.List;

public class ImageBuilder {
    private String bucket;
    private String mimeType;
    private String fileName;
    private List<Tag> tags;

    private ImageBuilder() {
        this.tags = new ArrayList<>();
    }

    public static ImageBuilder newInstance() {
        return new ImageBuilder();
    }
    public ImageBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public ImageBuilder withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }
    public ImageBuilder withBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
    public ImageBuilder addTag(String key, String value) {
        this.tags.add(new Tag(key, value));
        return this;
    }

    public Image build() {
        if(bucket == null){
            throw new IllegalStateException("bucket should not be null");
        }
        Image productImage = new Image(bucket, mimeType, fileName, tags);
        return productImage;
    }
}

