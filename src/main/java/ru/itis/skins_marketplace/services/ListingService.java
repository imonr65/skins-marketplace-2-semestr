package ru.itis.skins_marketplace.services;

import org.springframework.data.domain.Page;
import ru.itis.skins_marketplace.dto.ListingCreateDto;
import ru.itis.skins_marketplace.dto.ListingDto;
import ru.itis.skins_marketplace.dto.request.MarketFilterDto;
import ru.itis.skins_marketplace.dto.response.ListingCardMarketDto;
import ru.itis.skins_marketplace.models.Listing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ListingService {

    void createListing(Long sellerId, ListingCreateDto dto);

    ListingDto createListingByAdmin(ListingDto dto);

    Map<Long, Listing> getActiveListingsForSkinIds(List<Long> skinIds);

    List<Listing> getAllActiveOrderedListings(Long skinTemplateId);

    List<Listing> getAll();

    List<Listing> getAllActiveListingsBySeller(Long sellerId);

    Page<ListingCardMarketDto> getAllLisingsByPage(int page, int size);

    Listing findBySkinId(Long skinId);

    List<Listing> getListingsByFilter(MarketFilterDto dto);

    void updateListingPrice(Long listingId, BigDecimal newPrice, Long sellerId);

    void deleteListing(Long listingId, Long sellerId);
}