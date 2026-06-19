package ru.itis.skins_marketplace.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SkinInfo(@JsonProperty("image") String imageUrl) {
}
