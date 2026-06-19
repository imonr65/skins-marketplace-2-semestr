package ru.itis.skins_marketplace.fishki.first.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashbackSubscribeResponse {

    private String typeName;

    private BigDecimal price;
}
