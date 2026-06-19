package ru.itis.skins_marketplace.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkinDto {

    @NotNull
    @DecimalMin(value = "0.0001")
    @DecimalMax(value = "0.9999")
    private Double skinFloat;

    private Long skinTemplateId;

    private Long userInventoryId;
}
