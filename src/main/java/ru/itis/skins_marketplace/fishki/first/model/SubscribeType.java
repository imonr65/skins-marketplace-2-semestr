package ru.itis.skins_marketplace.fishki.first.model;

import lombok.Getter;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Getter
public enum SubscribeType {
    WITHOUT(BigDecimal.ZERO),
    COMMON(BigDecimal.valueOf(299.0)),
    SILVER(BigDecimal.valueOf(599.0)),
    GOLD(BigDecimal.valueOf(999.0));

    private final BigDecimal price;

    SubscribeType(BigDecimal price) {
        this.price = price;
    }

    public static List<SubscribeType> getNonWithout() {
        return Arrays.stream(values())
                .filter(t -> t != WITHOUT)
                .toList();
    }
}
