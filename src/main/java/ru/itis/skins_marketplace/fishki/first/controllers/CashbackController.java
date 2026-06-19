package ru.itis.skins_marketplace.fishki.first.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.fishki.first.dto.CashbackSubscribeRequest;
import ru.itis.skins_marketplace.fishki.first.services.CashbackService;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subscribes")
public class CashbackController {

    private final CashbackService cashbackService;

    @GetMapping
    public String getSubscribesPage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("subscribes", cashbackService.getAllSubscribes());
        return "subscribes_page";
    }

    @PostMapping("/subscribe")
    public String userSubscribed(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, CashbackSubscribeRequest dto) {
        cashbackService.userSubscribed(userDetails.getUser().getId(), dto);
        return "redirect:/profile";
    }
}
