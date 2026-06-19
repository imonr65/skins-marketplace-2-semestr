package ru.itis.skins_marketplace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkinTemplateUpdateDto {

    private String name;

    private String weaponCategory;

    private String weaponType;

    private String rarity;

    private String wear;

    private String imageUrl;
}
