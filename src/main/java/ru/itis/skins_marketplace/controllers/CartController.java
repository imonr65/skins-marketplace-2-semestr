package ru.itis.skins_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.request.BuyCartRequest;
import ru.itis.skins_marketplace.mappers.user.UserEntityToProfileDto;
import ru.itis.skins_marketplace.models.CartItem;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.CartService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final UserEntityToProfileDto headerDtoMapper;


    @GetMapping
    public String getCartPage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        List<CartItem> cartItems = cartService.getAllOrderedUsersCartItems(userDetails.getUser().getId());
        model.addAttribute("user", headerDtoMapper.toDto(userDetails.getUser()));
        model.addAttribute("cartItems", cartItems);
        return "cart_page";
    }

    @PostMapping("/buy")
    public void buyAllItemsInCart(@AuthenticationPrincipal CustomUserDetailsImpl customUserDetails,
                                  BuyCartRequest buyCartRequest) {

    }
}
