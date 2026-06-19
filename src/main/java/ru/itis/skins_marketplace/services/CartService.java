package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.dto.request.BuyCartRequest;
import ru.itis.skins_marketplace.dto.request.CartItemDtoRequest;
import ru.itis.skins_marketplace.models.CartItem;

import java.util.List;

public interface CartService {

    CartItem createCartItem(CartItemDtoRequest dto);

    List<CartItem> getAllOrderedUsersCartItems(Long userId);

    void buyCartItems(Long userId, BuyCartRequest buyCartRequest);

    void deleteCartById(Long cartId);
}
