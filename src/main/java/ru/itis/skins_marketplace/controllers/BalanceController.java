package ru.itis.skins_marketplace.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.request.BalanceUpUserRequest;
import ru.itis.skins_marketplace.mappers.user.UserEntityToProfileDto;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.BalanceUserService;
import ru.itis.skins_marketplace.services.UserService;

@Controller
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceUserService balanceUserService;

    private final UserEntityToProfileDto headerDtoMapper;

    private final UserService userService;

    @GetMapping
    public String getBalancePage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("user", headerDtoMapper.toDto(user));
        return "balance_page";
    }

    @PostMapping
    public String topUpUserBalance(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, @Valid BalanceUpUserRequest request) {
        balanceUserService.topUpUserBalance(userDetails.getUser().getId(), request);
        return "redirect:/balance";
    }
}
