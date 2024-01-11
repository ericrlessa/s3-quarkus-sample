package ecomarkets.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Tag (@Column(name = "tag_key") String key, String value){

    public Tag{
        if(key == null || key.isBlank()){
            throw new IllegalArgumentException("key is null");
        }
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("value is null");
        }
    }
    public static Tag of(String key, String value){
        return new Tag(key, value);
    }
}
