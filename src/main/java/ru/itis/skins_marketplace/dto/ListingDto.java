package ru.itis.skins_marketplace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingDto {

    private Long id;

    @NotBlank
    @Email
    @Size(min = 10, max = 70, message = "Неверное значение для значения почты")
    private String userEmail;

    @NotNull
    @DecimalMin(value = "0.0001")
    @DecimalMax(value = "0.9999")
    private Double skinFloat;

    @NotBlank
    private String skinTemplateName;

    @Min(1)
    @Max(100000)
    private Long price;
}
