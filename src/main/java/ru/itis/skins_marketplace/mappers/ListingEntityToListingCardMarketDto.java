package ru.itis.skins_marketplace.mappers;

import org.springframework.stereotype.Component;
import ru.itis.skins_marketplace.dto.response.ListingCardMarketDto;
import ru.itis.skins_marketplace.models.Listing;

@Component
public class ListingEntityToListingCardMarketDto {

    public ListingCardMarketDto toDto(Listing listing) {
        return ListingCardMarketDto.builder()
                .skin(listing.getSkin())
                .price(listing.getPrice())
                .build();
    }
}
