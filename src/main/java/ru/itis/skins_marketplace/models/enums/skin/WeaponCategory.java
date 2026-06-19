package ru.itis.skins_marketplace.models.enums.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeaponCategory {
    RIFLE("RIFLE"),
    PISTOL("PISTOL"),
    KNIFE("KNIFE"),
    SMG("SMG"),
    HEAVY("Heavy");

    private final String categoryName;

}
