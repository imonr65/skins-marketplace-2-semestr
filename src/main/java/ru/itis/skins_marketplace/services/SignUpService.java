package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.dto.request.SignUpForm;

public interface SignUpService {

    void addUser(SignUpForm form);
}
