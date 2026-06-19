package ru.itis.skins_marketplace.exceptions;

import org.springframework.stereotype.Component;

import java.io.Serial;

@Component
public class SkinTemplateAlreadyExistsException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public SkinTemplateAlreadyExistsException() {
        super("Шаблон скина уже существует");
    }

    public SkinTemplateAlreadyExistsException(String message) {
        super(message);
    }
}
