package ru.itis.skins_marketplace.services.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.skins_marketplace.dto.request.SignUpForm;
import ru.itis.skins_marketplace.models.LogEvent;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.models.UserInventory;
import ru.itis.skins_marketplace.models.enums.Role;
import ru.itis.skins_marketplace.models.enums.Type;
import ru.itis.skins_marketplace.repositories.UserRepository;
import ru.itis.skins_marketplace.services.LogEventService;
import ru.itis.skins_marketplace.services.MailService;
import ru.itis.skins_marketplace.services.SignUpService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final LogEventService logEventService;

    @Override
    public void addUser(@Valid SignUpForm form) {
        User user = User.builder()
                .name(form.getNickname())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .role(Role.ADMIN)
                .balance(BigDecimal.valueOf(0.0))
                .confirmed("NOT_CONFIRMED")
                .confirmCode(UUID.randomUUID().toString())
                .build();

        UserInventory userInventory = UserInventory.builder()
                .inventorySkins(new HashSet<>())
                .user(user)
                .build();
        user.setUserInventory(userInventory);
        userRepository.save(user);
        log.debug("Заполнили инфу об user-e: {}", user);
        mailService.sendEmailForConfirm(user.getEmail(), user.getConfirmCode());
        logEventService.save(LogEvent.builder()
                .method(this.getClass().getSimpleName() + ".addUser(SignUpForm form)")
                .text("Успешная регистарция пользователя")
                .createAt(OffsetDateTime.now())
                .type(Type.SUCCESS)
                .build()
        );
        log.info("Ргистрация прошла успешно");
    }
}
