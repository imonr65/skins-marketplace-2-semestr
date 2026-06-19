package ru.itis.skins_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class WeaponTypeDto {

    private String typeName;
}
