package ru.itis.skins_marketplace.exceptions;

public class ListingNotFoundException extends RuntimeException {
    public ListingNotFoundException(String message) {
        super(message);
    }

    public ListingNotFoundException() {
        super("Объявления не существует");
    }
}
