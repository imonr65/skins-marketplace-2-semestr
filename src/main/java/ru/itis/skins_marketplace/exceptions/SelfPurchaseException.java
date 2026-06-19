package ru.itis.skins_marketplace.exceptions;

public class SelfPurchaseException extends RuntimeException {
    public SelfPurchaseException(String message) {
        super(message);
    }

    public SelfPurchaseException() {
        super("Вы не можете купить свой же скин");
    }
}
