package ru.itis.skins_marketplace.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.skins_marketplace.dto.request.SkinTemplateDto;
import ru.itis.skins_marketplace.dto.users.UserDto;
import ru.itis.skins_marketplace.dto.users.UserProfileDto;
import ru.itis.skins_marketplace.dto.users.UserRoleForm;
import ru.itis.skins_marketplace.dto.users.UserSkinDto;
import ru.itis.skins_marketplace.exceptions.UserAlreadyExistsException;
import ru.itis.skins_marketplace.exceptions.UserNotFoundException;
import ru.itis.skins_marketplace.models.*;
import ru.itis.skins_marketplace.models.enums.Role;
import ru.itis.skins_marketplace.models.enums.Type;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;
import ru.itis.skins_marketplace.repositories.SkinRepository;
import ru.itis.skins_marketplace.repositories.SkinTemplateRepository;
import ru.itis.skins_marketplace.repositories.UserRepository;
import ru.itis.skins_marketplace.services.LogEventService;
import ru.itis.skins_marketplace.services.SkinTemplateService;
import ru.itis.skins_marketplace.services.UserService;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SkinRepository skinRepository;

    private final SkinTemplateService skinTemplateService;

    private final SkinTemplateRepository skinTemplateRepository;

    private final LogEventService logEventService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto save(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            logEventService.save(LogEvent.builder()
                            .text("Пользователь с email " + userDto.getEmail() + " уже существует")
                            .method(this.getClass().getSimpleName() + ".save(UserDto userDto)")
                            .createAt(OffsetDateTime.now())
                            .type(Type.ERROR)
                    .build());
            throw new UserAlreadyExistsException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }

        User user = User.builder()
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getNickname())
                .role(Role.valueOf(userDto.getRole()))
                .email(userDto.getEmail())
                .balance(userDto.getBalance())
                .userInventory(new UserInventory())
                .build();
        User savedUser = userRepository.save(user);
        return UserDto.builder()
                .id(savedUser.getId())
                .nickname(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .balance(savedUser.getBalance())
                .build();
    }

    @Transactional
    public UserSkinDto saveUserWithSkin(UserSkinDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь с email " + dto.getEmail() + " не найден");
        }
        User user = optionalUser.get();

        Skin skin = Skin.builder()
                .skinFloat(dto.getSkinFloat())
                .userInventory(user.getUserInventory())
                .build();

        Optional<SkinTemplate> skinTemplateOptional =
                skinTemplateRepository.findSkinTemplateByNameAndRarityAndWeaponType(
          dto.getName(), Rarity.valueOf(dto.getRarity()), WeaponType.valueOf(dto.getWeaponType()));

        SkinTemplate skinTemplate;
        skinTemplate = skinTemplateOptional.orElseGet(() -> skinTemplateService.add(SkinTemplateDto.builder()
                .name(dto.getName())
                .rarity(dto.getRarity())
                .wear(skin.getWear().toString())
                .weaponType(dto.getWeaponType())
                .weaponCategory(dto.getWeaponCategory())
                .build()));
        skin.setTemplate(skinTemplate);
        skinRepository.save(skin);
        dto.setWear(skin.getWear().getWearName());
        return dto;
    }

    @Override
    public User findByConfirmedCode(String confirmCode) {
        return userRepository.findByConfirmCode(confirmCode)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public void changeRole(UserRoleForm form) {
        log.debug("Изменение роли по email пользователя: {}", form.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(form.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.debug("Новая роль пользователя: {}", form.getRole());
            user.setRole(Role.valueOf(form.getRole()));
            userRepository.updateRoleByEmail(user.getEmail(), user.getRole());
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findByIdWithInventory(Long id) {
        log.debug("UserService- метод: findByIdWithInventory - Id пользователя: {}", id);
        return userRepository.findByIdWithInventory(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateProfile(Long id, UserProfileDto userProfileDto) {
        log.debug("UserService - начало метода updateProfile");
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = optionalUser.get();
        String oldName = user.getName();
        if (userProfileDto.getName() != null) {
            user.setName(userProfileDto.getName());
        }
        if (userProfileDto.getEmail() != null) {
            user.setEmail(userProfileDto.getEmail());
        }
        userRepository.save(user);
        log.debug("Успешно изменены данные профиля у пользователя под именем: {}", oldName);
    }

    @Override
    public Set<SkinTemplate> getFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return user.getFavoritesSkins();
    }

    @Override
    public void addToFavorites(Long userId, Long skinTemplateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        SkinTemplate skinTemplate = skinTemplateRepository.findById(skinTemplateId)
                .orElseThrow();

        user.getFavoritesSkins().add(skinTemplate);

        userRepository.save(user);
    }

    @Override
    public void removeFromFavorites(Long userId, Long skinTemplateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.getFavoritesSkins()
                .removeIf(skin -> skin.getId().equals(skinTemplateId));

        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }
}
