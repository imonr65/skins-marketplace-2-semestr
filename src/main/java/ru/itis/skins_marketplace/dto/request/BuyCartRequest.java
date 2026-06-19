package ru.itis.skins_marketplace.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BuyCartRequest {
    private List<Long> cartItemIds;
}
