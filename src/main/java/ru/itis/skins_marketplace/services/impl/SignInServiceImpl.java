package ru.itis.skins_marketplace.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.skins_marketplace.dto.request.SignInForm;
import ru.itis.skins_marketplace.exceptions.NotMatchedPasswordException;
import ru.itis.skins_marketplace.exceptions.UserNotFoundException;
import ru.itis.skins_marketplace.models.LogEvent;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.models.enums.Type;
import ru.itis.skins_marketplace.repositories.UserRepository;
import ru.itis.skins_marketplace.services.LogEventService;
import ru.itis.skins_marketplace.services.SignInService;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final LogEventService logEventService;

    @Override
    public void signIn(SignInForm signInForm) {
        Optional<User> optionalUser = userRepository.findByEmail(signInForm.getEmail());
        if (optionalUser.isPresent()) {
            boolean matched = passwordEncoder.matches(signInForm.getPassword(), optionalUser.get().getPassword());
            if (matched) {
               logEventService.save(LogEvent.builder()
                               .text("Успешная авторизация пользователя")
                               .method(this.getClass().getSimpleName() + ".signIn(SignInForm signInForm)")
                               .createAt(OffsetDateTime.now())
                               .type(Type.SUCCESS)
                       .build());
            } else {
                logEventService.save(LogEvent.builder()
                        .text("Пользователь ввёл неправильный пароль")
                        .method(this.getClass().getSimpleName() + ".signIn(SignInForm signInForm)")
                        .createAt(OffsetDateTime.now())
                        .type(Type.ERROR)
                        .build());
                throw new NotMatchedPasswordException();
            }
        } else {
            logEventService.save(LogEvent.builder()
                    .text("Пользователь не найден")
                    .method(this.getClass().getSimpleName() + ".signIn(SignInForm signInForm)")
                    .createAt(OffsetDateTime.now())
                    .type(Type.ERROR)
                    .build());
            throw new UserNotFoundException();
        }
    }
}
