package ru.itis.skins_marketplace.fishki.first.services;

import ru.itis.skins_marketplace.fishki.first.dto.CashbackSubscribeRequest;
import ru.itis.skins_marketplace.fishki.first.dto.CashbackSubscribeResponse;
import ru.itis.skins_marketplace.fishki.first.model.CashbackSubscribe;


import java.math.BigDecimal;
import java.util.List;

public interface CashbackService {

    void deleteByType(Long userId, CashbackSubscribeRequest dto);

    BigDecimal getUserCashback(Long userId, BigDecimal priceForPurchase);

    List<CashbackSubscribeResponse> getAllSubscribes();

    CashbackSubscribe userSubscribed(Long userId, CashbackSubscribeRequest dto);
}
