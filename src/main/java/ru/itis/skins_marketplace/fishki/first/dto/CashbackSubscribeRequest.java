package ru.itis.skins_marketplace.fishki.first.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CashbackSubscribeRequest {

    private String typeName;

    private Long duration;
}
