package ru.itis.skins_marketplace.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.skins_marketplace.dto.request.BalanceUpUserRequest;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.services.BalanceUserService;
import ru.itis.skins_marketplace.services.UserService;

@Service
@RequiredArgsConstructor
public class BalanceUserServiceImpl implements BalanceUserService {

    private final UserService userService;

    @Override
    public void topUpUserBalance(Long userId, BalanceUpUserRequest request) {
        User user = userService.findById(userId);
        user.setBalance(user.getBalance().add(request.getSumToTopUpUserBalance()));
        userService.update(user);
    }
}
