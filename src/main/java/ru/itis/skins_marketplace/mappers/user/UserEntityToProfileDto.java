package ru.itis.skins_marketplace.mappers.user;

import org.springframework.stereotype.Component;
import ru.itis.skins_marketplace.dto.users.UserProfileDto;
import ru.itis.skins_marketplace.models.User;

@Component
public class UserEntityToProfileDto {

    public UserProfileDto toDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .balance(user.getBalance())
                .cashbackSubscribe(user.getCashbackSubscribe())
                .build();
    }
}
