package ru.itis.skins_marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarketFilterDto {

    private String searchQuery;

    private String weaponCategory;

    private String weaponType;

    private String rarity;

    private String wear;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;
}
