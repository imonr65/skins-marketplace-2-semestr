package ru.itis.skins_marketplace.dto.users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String nickname;

    @NotBlank
    @Email
    @Size(min = 10, max = 70, message = "Неверное значение для значения почты")
    private String email;

    @NotBlank
    @Size(min = 8, max = 100, message = "Длина пароля выходит за рамки дозволенного")
    private String password;

    private String role;

    @Min(0)
    @Max(1_000_000)
    private BigDecimal balance;

}
