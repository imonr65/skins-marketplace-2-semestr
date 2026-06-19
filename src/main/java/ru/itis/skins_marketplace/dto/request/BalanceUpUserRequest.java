package ru.itis.skins_marketplace.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceUpUserRequest {

    @Min(1)
    private BigDecimal sumToTopUpUserBalance;
}
