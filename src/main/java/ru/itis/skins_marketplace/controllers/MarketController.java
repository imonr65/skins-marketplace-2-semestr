package ru.itis.skins_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.skins_marketplace.dto.request.MarketFilterDto;
import ru.itis.skins_marketplace.dto.response.ListingCardMarketDto;
import ru.itis.skins_marketplace.mappers.user.UserEntityToHeaderDto;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WEAR;
import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.ListingService;
import ru.itis.skins_marketplace.services.UserService;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final ListingService listingService;
    private final UserEntityToHeaderDto mapper;
    private final UserService userService;

    @GetMapping
    public String getMarketPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                @AuthenticationPrincipal CustomUserDetailsImpl userDetails,
                                Model model) {
        Page<ListingCardMarketDto> listings = listingService.getAllLisingsByPage(page, size);
        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("listings", listings.getContent());
        model.addAttribute("user", mapper.toDto(user));
        model.addAttribute("weaponCategories", WeaponCategory.values());
        model.addAttribute("weaponTypes", WeaponType.values());
        model.addAttribute("rarities", Rarity.values());
        model.addAttribute("wears", WEAR.values());

        return "market_page";
    }

    @GetMapping("/filter")
    public String filterListings(MarketFilterDto dto, Model model) {
        List<Listing> filteredListings = listingService.getListingsByFilter(dto);
        model.addAttribute("listings", filteredListings);
        return "listings-fragment";
    }
}
