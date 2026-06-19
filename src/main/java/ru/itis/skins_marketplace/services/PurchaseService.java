package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.dto.request.SellingListingHistoryDto;
import ru.itis.skins_marketplace.models.CartItem;

import java.util.List;

public interface PurchaseService {

    void quickPurchase(Long listingId, Long userId);

    void cartPurchases(Long userId, List<CartItem> cartItems);

    List<SellingListingHistoryDto> getPriceHistory(Long skinTemplateId);
}
