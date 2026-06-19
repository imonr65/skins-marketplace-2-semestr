package ru.itis.skins_marketplace.dto.users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.skins_marketplace.fishki.first.model.CashbackSubscribe;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {

    private Long id;

    private String imageUrl;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Size(min = 10, max = 100)
    private String email;

    @Min(0)
    @Max(1_000_000)
    private BigDecimal balance;

    private CashbackSubscribe cashbackSubscribe;

}
