package ru.itis.skins_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.skins_marketplace.mappers.user.UserEntityToProfileDto;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.ListingService;
import ru.itis.skins_marketplace.services.UserService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;

    private final UserService userService;

    private final UserEntityToProfileDto mapper;


    @GetMapping("/my")
    public String getUserSellingSkins(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        List<Listing> userListings = listingService.getAllActiveListingsBySeller(userDetails.getUser().getId());
        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("user", mapper.toDto(user));
        model.addAttribute("userListings", userListings);
        return "user_active_listings_page";
    }

    @PostMapping("/{listingId}/update-price")
    public String updateListingPrice(@AuthenticationPrincipal CustomUserDetailsImpl userDetails,
                                     @PathVariable Long listingId,
                                     @RequestParam BigDecimal price) {
        listingService.updateListingPrice(listingId, price, userDetails.getUser().getId());
        return "redirect:/listings/my";
    }

    @PostMapping("/{listingId}/delete-listing")
    public String deleteListing(@AuthenticationPrincipal CustomUserDetailsImpl userDetails,
                                @PathVariable Long listingId) {
        listingService.deleteListing(listingId, userDetails.getUser().getId());
        return "redirect:/listings/my";
    }

}