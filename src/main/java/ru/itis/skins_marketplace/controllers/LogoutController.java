package ru.itis.skins_marketplace.controllers;

import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout-success")
    public String getLogoutPage() {
        return "logout_page";
    }
}
