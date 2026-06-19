package ru.itis.skins_marketplace.exceptions;

public class UserHasNotEnoughMoneyOnBalanceException extends RuntimeException {
    public UserHasNotEnoughMoneyOnBalanceException(String message) {
        super(message);
    }

    public UserHasNotEnoughMoneyOnBalanceException() {
        super("У вас недостаточно средств");
    }
}
