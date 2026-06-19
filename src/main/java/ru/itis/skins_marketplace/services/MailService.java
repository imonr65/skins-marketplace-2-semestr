package ru.itis.skins_marketplace.services;

public interface MailService {

    void sendEmailForConfirm(String email, String code);
}
