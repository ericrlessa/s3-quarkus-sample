package ecomarkets.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Immutable
public class ProductImage extends PanacheEntity {

    private String bucket;
    private String mimetype;
    private String filename;
    @Transient
    private List<Tag> tags;

    ProductImage(String bucket, String mimetype, String filename, List<Tag> tags) {
        this.bucket = bucket;
        this.mimetype = mimetype;
        this.filename = filename;
        this.tags = tags;
    }

    private ProductImage(){
    }

    public ProductImage addTag(String key, String value){
        tags.add(new Tag(key, value));
        return this;
    }
    public String bucket(){
        return this.bucket;
    }

    public String fileName(){ return this.filename; }

    public String mimeType(){ return this.mimetype; }

    public List<Tag> tags(){
        return this.tags;
    }

    public String key(){
        return id.toString();
    }

}
