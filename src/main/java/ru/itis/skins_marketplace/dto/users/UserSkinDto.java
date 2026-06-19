package ru.itis.skins_marketplace.dto.users;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkinDto {

    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String weaponCategory;
    @NotBlank
    private String weaponType;
    @NotBlank
    private String rarity;
    private String wear;

    @NotNull
    @DecimalMin(value = "0.0001")
    @DecimalMax(value = "0.9999")
    private double skinFloat;

}
