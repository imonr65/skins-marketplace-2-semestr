package ru.itis.skins_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.services.UserService;

@Controller()
@RequiredArgsConstructor
public class ConfirmController {

    private final UserService userService;

    @GetMapping("/confirm/{code}")
    public String confirmUser(@PathVariable String code) {
        User user = userService.findByConfirmedCode(code);
        user.setConfirmed("CONFIRMED");
        userService.update(user);
        return "redirect:/";
    }
}
