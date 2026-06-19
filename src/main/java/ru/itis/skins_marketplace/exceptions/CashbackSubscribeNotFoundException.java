package ru.itis.skins_marketplace.exceptions;

public class CashbackSubscribeNotFoundException extends RuntimeException {
    public CashbackSubscribeNotFoundException(String message) {
        super(message);
    }

    public CashbackSubscribeNotFoundException() {
        super("Подписка не найдена");
    }
}
