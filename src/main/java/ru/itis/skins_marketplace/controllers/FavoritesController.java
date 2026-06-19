package ru.itis.skins_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.UserService;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/likes")
public class FavoritesController {

    private final UserService userService;


    @PostMapping("/add/{skinTemplateId}")
    public String addToFavorites(@PathVariable Long skinTemplateId,
                                 @AuthenticationPrincipal CustomUserDetailsImpl userDetails) {

        userService.addToFavorites(userDetails.getUser().getId(), skinTemplateId);

        return "redirect:/market";
    }

    @PostMapping("/remove/{skinTemplateId}")
    public String removeFromFavorites(@PathVariable Long skinTemplateId,
                                      @AuthenticationPrincipal CustomUserDetailsImpl userDetails) {

        userService.removeFromFavorites(userDetails.getUser().getId(), skinTemplateId);

        return "redirect:/likes";
    }

    @GetMapping
    public String favoritesPage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails,
                                Model model) {

        Set<SkinTemplate> favorites =
                userService.getFavorites(userDetails.getUser().getId());

        model.addAttribute("favorites", favorites);

        return "favorites_page";
    }
}
