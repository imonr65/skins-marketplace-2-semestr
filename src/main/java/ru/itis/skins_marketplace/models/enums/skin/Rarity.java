package ru.itis.skins_marketplace.models.enums.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rarity{
    Red("RED"),
    Pink("PINK"),
    Blue("BLUE"),
    Gray("GRAY"),
    GOLDEN("GOLDEN");

    private final String rareName;
}
