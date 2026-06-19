package ru.itis.skins_marketplace.exceptions;

public class SkinNotFoundException extends RuntimeException{

    public SkinNotFoundException() {
        super("Скин не найден");
    }

    public SkinNotFoundException(String message) {
        super(message);
    }
}
