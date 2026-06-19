package ru.itis.skins_marketplace.models.enums.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WEAR {
    FACTORY_NEW("FACTORY_NEW"),
    MINIMAL_WEAR("MINIMAL_WEAR"),
    FIELD_TESTED("FIELD_TESTED"),
    WELL_WORN("WELL_WORN"),
    BATTLE_SCARED("BATTLE_SCARED");

    private final String wearName;
}
