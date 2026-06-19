package ru.itis.skins_marketplace.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.request.SignUpForm;
import ru.itis.skins_marketplace.services.SignUpService;


@RequiredArgsConstructor
@Controller
@RequestMapping("/signUp")
@Slf4j
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping
    public String getSignUpPage() {
        return "sign_up_page";
    }

    @PostMapping
    public String signUp(@Valid SignUpForm signUpForm, BindingResult bindingResult) {
        log.debug("начало регистрации: {}", signUpForm);
        if (bindingResult.hasErrors()) {
            return "sign_up_page";
        }
        log.debug("Слой контроллера. Регистрация пользователя с ником: {}", signUpForm.getNickname());
        signUpService.addUser(signUpForm);
        return "redirect:/";
    }
}
