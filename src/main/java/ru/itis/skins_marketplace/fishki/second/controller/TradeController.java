package ru.itis.skins_marketplace.fishki.second.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.fishki.second.TradeSkinsDto;
import ru.itis.skins_marketplace.fishki.second.services.TradeService;
import ru.itis.skins_marketplace.models.Skin;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.models.enums.ListingStatus;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.UserService;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {

    private final UserService userService;

    private final TradeService tradeService;

    @GetMapping
    public String getTradePage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        User user = userService.findByIdWithInventory(userDetails.getUser().getId());

        Set<Skin> inventorySkins = user.getUserInventory().getInventorySkins();

        Set<Skin> availableSkins = inventorySkins.stream()
                .filter(skin -> skin.getListing().stream()
                        .noneMatch(listing -> listing.getListingStatus() == ListingStatus.ACTIVE))
                .collect(Collectors.toSet());

        model.addAttribute("userInventorySkins", availableSkins);

        return "trade_page";
    }

    @PostMapping
    public String tradeUserSkinsForOneSkin(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, TradeSkinsDto dto) {
        tradeService.tradeUserSkinsForOneHigherRareSkin(userDetails.getUser().getId(), dto);
        return "redirect:/profile";
    }
}
