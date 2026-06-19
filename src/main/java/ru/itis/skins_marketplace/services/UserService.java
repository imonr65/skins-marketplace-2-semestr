package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.dto.users.UserDto;
import ru.itis.skins_marketplace.dto.users.UserProfileDto;
import ru.itis.skins_marketplace.dto.users.UserRoleForm;
import ru.itis.skins_marketplace.dto.users.UserSkinDto;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.User;

import java.util.Set;

public interface UserService {

    UserDto save(UserDto userDto);

    UserSkinDto saveUserWithSkin(UserSkinDto dto);

    User findByConfirmedCode(String confirmedCode);

    User findById(Long id);

    void changeRole(UserRoleForm form);

    User findByIdWithInventory(Long id);

    void updateProfile(Long id, UserProfileDto userProfileDto);

    void update(User user);

    Set<SkinTemplate> getFavorites(Long userId);

    void addToFavorites(Long userId, Long skinTemplateId);

    void removeFromFavorites(Long userId, Long skinTemplateId);

}
