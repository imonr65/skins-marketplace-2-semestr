package ru.itis.skins_marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.skins_marketplace.models.Skin;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListingCardMarketDto {

    private Long listingId;

    private Skin skin;

    private BigDecimal price;
}
