package ru.itis.skins_marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
public class SellingListingHistoryDto {

    private OffsetDateTime soldAt;

    private BigDecimal price;

    private String soldAtStr;

}
