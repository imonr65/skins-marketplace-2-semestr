package ru.itis.skins_marketplace.exceptions;

import java.io.Serial;

public class SkinTemplateNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2L;

    public SkinTemplateNotFoundException() {
        super("Шаблон скина не найден");
    }

    public SkinTemplateNotFoundException(String message) {
        super(message);
    }
}
