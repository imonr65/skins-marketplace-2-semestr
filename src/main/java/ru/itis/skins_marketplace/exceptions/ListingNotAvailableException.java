package ru.itis.skins_marketplace.exceptions;

public class ListingNotAvailableException extends RuntimeException {
    public ListingNotAvailableException(String message) {
        super(message);
    }

  public ListingNotAvailableException() {
      super("Объявление либо зарезервировано, либо продано");
  }
}
