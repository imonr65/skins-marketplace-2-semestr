package ru.itis.skins_marketplace.models.enums.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeaponType {

    GLOCK("GLOCK"),
    USPS("USPS"),
    DEAGLE("DEAGLE"),

    // === Пистолеты-пулемёты ===
    P90 ("P90"),

    // === Винтовки ===
    AK47("AK47"),
    M4A4("M4A4"),

    // === Снайперские винтовки ===
    AWP("AWP"),

    // === Ножи ===
    KNIFE_KARAMBIT("KNIFE_KARAMBIT"),
    KNIFE_BUTTERFLY("KNIFE_BUTTERFLY");

    private final String weaponName;
}
