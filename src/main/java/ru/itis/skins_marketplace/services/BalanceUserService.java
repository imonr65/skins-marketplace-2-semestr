package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.dto.request.BalanceUpUserRequest;

public interface BalanceUserService {

    void topUpUserBalance(Long userId, BalanceUpUserRequest request);
}
