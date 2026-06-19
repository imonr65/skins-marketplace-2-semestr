package ru.itis.skins_marketplace.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHeaderDto {

    private String name;

    private String email;

    private String imageUrl;

    private BigDecimal balance;
}
