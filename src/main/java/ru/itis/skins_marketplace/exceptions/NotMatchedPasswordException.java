package ru.itis.skins_marketplace.exceptions;

public class NotMatchedPasswordException extends RuntimeException {
    public NotMatchedPasswordException(String message) {
        super(message);
    }

    public NotMatchedPasswordException() {
        super("Вы ввели неправильный парроль");
    }
}
