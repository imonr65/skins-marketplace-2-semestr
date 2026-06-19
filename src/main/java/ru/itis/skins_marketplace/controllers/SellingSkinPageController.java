package ru.itis.skins_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.skins_marketplace.dto.request.CartItemDtoRequest;
import ru.itis.skins_marketplace.dto.request.SellingListingHistoryDto;
import ru.itis.skins_marketplace.dto.request.SellingSkinTemplateDto;
import ru.itis.skins_marketplace.mappers.user.UserEntityToProfileDto;
import ru.itis.skins_marketplace.models.*;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/market/{weaponCategory}/{weaponType}")
public class SellingSkinPageController {

    private final SkinTemplateService skinTemplateService;

    private final SkinService skinService;

    private final ListingService listingService;

    private final UserService userService;

    private final PurchaseService purchaseService;

    private final UserEntityToProfileDto mapper;

    private final CartService cartService;

    @GetMapping("/{slug}")
    public String getSellingSkinPage(@PathVariable String weaponCategory,
                                     @PathVariable String weaponType,
                                     @PathVariable String slug,
                                     @AuthenticationPrincipal CustomUserDetailsImpl userDetails,
                                     Model model) {

        User user = userService.findById(userDetails.getUser().getId());

        SellingSkinTemplateDto dto = SellingSkinTemplateDto.builder()
                .weaponCategory(weaponCategory)
                .weaponType(weaponType)
                .slug(slug)
                .build();
        SkinTemplate skinTemplate = skinTemplateService.findByWeaponCategoryAndTypeAndSlug(dto);

        List<SellingListingHistoryDto> history = purchaseService.getPriceHistory(skinTemplate.getId());
        log.debug("priceHistory size: {}", history.size());
        log.debug("history: {}", history);
        List<Listing> orderedListings = listingService.getAllActiveOrderedListings(skinTemplate.getId());
        Listing listingWithSmallestPrice = orderedListings.isEmpty() ? null : orderedListings.getFirst();
        model.addAttribute("user", mapper.toDto(user));
        model.addAttribute("skinTemplate", skinTemplate);
        model.addAttribute("listingWithSmallestPrice", listingWithSmallestPrice);
        model.addAttribute("orderedListings", orderedListings);
        model.addAttribute("priceHistory", history);
        return "selling_skin_template_page";
    }

    @PostMapping("{slug}/fastBuy")
    public String fastBuy(@PathVariable String weaponCategory,
                                     @PathVariable String weaponType,
                                     @PathVariable String slug,
                                     @RequestParam("listingId") Long listingId,
                                     @AuthenticationPrincipal CustomUserDetailsImpl userDetails) {
        log.debug("Быстрая покупка lsiting-а с id={}", listingId);
        purchaseService.quickPurchase(listingId, userDetails.getUser().getId());
        return "redirect:/market/" + weaponCategory + "/" + weaponType + "/" + slug;
    }

    @PostMapping("/{slug}/addInCart")
    public String addInCart(@PathVariable String weaponCategory,
                                        @PathVariable String weaponType,
                                        @PathVariable String slug,
                                        @RequestParam("listingId") Long listingId,
                                        @AuthenticationPrincipal CustomUserDetailsImpl customUserDetails) {

        CartItem cartItem = cartService.createCartItem(
                CartItemDtoRequest.builder()
                        .listingId(listingId)
                        .userId(customUserDetails.getUser().getId())
                        .build()
        );
        return "redirect:/market/" + weaponCategory + "/" + weaponType + "/" + slug;
    }

}
