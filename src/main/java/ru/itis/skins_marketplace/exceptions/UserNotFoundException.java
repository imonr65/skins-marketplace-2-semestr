package ru.itis.skins_marketplace.exceptions;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 3L;

    public UserNotFoundException() {
        super("Пользователь не найден");
    }

    public UserNotFoundException(String message) {super(message);}
}
