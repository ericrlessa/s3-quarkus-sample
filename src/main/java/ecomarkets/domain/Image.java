package ecomarkets.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Immutable
public class Image extends PanacheEntity {
    private String bucket;
    private String mimetype;
    private String filename;
    @ElementCollection
    private List<Tag> tags;

    Image(String bucket, String mimetype, String filename, List<Tag> tags) {
        this.bucket = bucket;
        this.mimetype = mimetype;
        this.filename = filename;
        this.tags = tags;
    }

    private Image(){
    }

    public Image addTag(String key, String value){
        tags.add(Tag.of(key, value));
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
