package ru.itis.skins_marketplace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInForm {

    @NotBlank
    @Email
    @Size(min = 10, max = 70, message = "Неверное значение для значения почты")
    private String email;

    @NotBlank
    @Size(min = 8, max = 100, message = "Длина пароля выходит за рамки дозволенного")
    private String password;
}
