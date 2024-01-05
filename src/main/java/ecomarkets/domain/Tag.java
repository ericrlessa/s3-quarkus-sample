package ecomarkets.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Tag (@Column(name = "tag_key") String key, String value){}
