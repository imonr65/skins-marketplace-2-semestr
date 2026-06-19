package ru.itis.skins_marketplace.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.request.SignInForm;
import ru.itis.skins_marketplace.services.SignInService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signIn")
public class SignInController {

    private final SignInService signInService;

    @GetMapping()
    public String getSignInPage() {
        return "sign_in_page";
    }

    @PostMapping
    public String signIn(@Valid SignInForm signInForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign_in_page";
        }
        signInService.signIn(signInForm);
        return "redirect:/";
    }
}
