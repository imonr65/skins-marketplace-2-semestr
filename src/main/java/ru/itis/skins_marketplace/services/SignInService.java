package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.dto.request.SignInForm;

public interface SignInService {

    void signIn(SignInForm signInForm);
}
