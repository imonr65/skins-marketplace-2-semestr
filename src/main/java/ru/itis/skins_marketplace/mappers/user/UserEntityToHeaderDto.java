package ru.itis.skins_marketplace.mappers.user;

import org.springframework.stereotype.Component;
import ru.itis.skins_marketplace.dto.users.UserHeaderDto;
import ru.itis.skins_marketplace.models.User;

@Component
public class UserEntityToHeaderDto {

    public UserHeaderDto toDto(User user) {
        return UserHeaderDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .balance(user.getBalance())
                .build();
    }

}
