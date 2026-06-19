package ru.itis.skins_marketplace.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException() {
        super("Пользователь уже существует");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
