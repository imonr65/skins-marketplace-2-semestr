package ru.itis.skins_marketplace.dto.api;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;

public record ApiSkinDto(Map<String, SkinInfo> skinInfoMap) {

    @JsonCreator
    public static ApiSkinDto fromProperties(Map<String, SkinInfo> map) {
        return new ApiSkinDto(map);
    }
}


