package ecomarkets.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductImageBuilder {
    private String bucket;
    private String mimeType;
    private String fileName;
    private List<Tag> tags;

    private ProductImageBuilder() {
        this.tags = new ArrayList<>();
    }

    public static ProductImageBuilder newInstance() {
        return new ProductImageBuilder();
    }
    public ProductImageBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public ProductImageBuilder withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }
    public ProductImageBuilder withBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
    public ProductImageBuilder addTag(String key, String value) {
        this.tags.add(new Tag(key, value));
        return this;
    }

    public ProductImage build() {
        if(bucket == null){
            throw new IllegalStateException("bucket should not be null");
        }
        ProductImage productImage = new ProductImage(bucket, mimeType, fileName, tags);
        return productImage;
    }
}

