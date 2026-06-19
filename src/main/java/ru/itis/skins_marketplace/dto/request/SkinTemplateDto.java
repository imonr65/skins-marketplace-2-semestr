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
public class SkinTemplateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String weaponCategory;

    @NotBlank
    private String weaponType;

    @NotBlank
    private String rarity;

    @NotBlank
    private String wear;

    @NotBlank
    private String imageUrl;
}
