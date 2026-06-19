package ru.itis.skins_marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellingSkinTemplateDto {

    private String weaponCategory;

    private String weaponType;

    private String slug;
}
