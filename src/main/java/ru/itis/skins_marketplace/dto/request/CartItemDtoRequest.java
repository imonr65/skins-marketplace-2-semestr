package ru.itis.skins_marketplace.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDtoRequest {

    private Long listingId;

    private Long userId;
}
