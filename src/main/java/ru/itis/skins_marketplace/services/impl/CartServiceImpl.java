package ru.itis.skins_marketplace.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.skins_marketplace.dto.request.BuyCartRequest;
import ru.itis.skins_marketplace.dto.request.CartItemDtoRequest;
import ru.itis.skins_marketplace.exceptions.ListingNotFoundException;
import ru.itis.skins_marketplace.exceptions.SelfPurchaseException;
import ru.itis.skins_marketplace.exceptions.UserNotFoundException;
import ru.itis.skins_marketplace.models.CartItem;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.repositories.CartItemRepository;
import ru.itis.skins_marketplace.repositories.ListingRepository;
import ru.itis.skins_marketplace.repositories.UserRepository;
import ru.itis.skins_marketplace.services.CartService;
import ru.itis.skins_marketplace.services.PurchaseService;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;

    private final UserRepository userRepository;

    private final ListingRepository listingRepository;

    private final PurchaseService purchaseService;

    @Override
    @Transactional
    public void buyCartItems(Long userId, BuyCartRequest buyCartRequest) {
        List<CartItem> cartItems = new ArrayList<>();
        for (Long cartItemId: buyCartRequest.getCartItemIds()) {
             cartItems.add(cartItemRepository.findAllByUserIdAndId(userId, cartItemId));
        }

    }

    @Override
    public CartItem createCartItem(CartItemDtoRequest dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new);
        Listing listing = listingRepository.findById(dto.getListingId()).orElseThrow(ListingNotFoundException::new);

        if (Objects.equals(user.getId(), listing.getSeller().getId())) {
            throw new SelfPurchaseException();
        }

        return cartItemRepository.save(CartItem.builder()
                        .addedAt(OffsetDateTime.now())
                        .selected(true)
                        .quantity(1)
                        .user(user)
                        .listing(listing)
                        .priceAtAddition(listing.getPrice())
                        .build());
    }

    @Override
    public List<CartItem> getAllOrderedUsersCartItems(Long userId) {
        return cartItemRepository.getOrderedCartItemsByUserId(userId);
    }


    @Override
    public void deleteCartById(Long cartId) {
        cartItemRepository.deleteById(cartId);
    }
}
