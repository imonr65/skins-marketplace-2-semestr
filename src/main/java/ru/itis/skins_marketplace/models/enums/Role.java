package ru.itis.skins_marketplace.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;
}
