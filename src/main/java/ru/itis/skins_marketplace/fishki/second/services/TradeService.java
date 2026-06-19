package ru.itis.skins_marketplace.fishki.second.services;

import ru.itis.skins_marketplace.fishki.second.TradeSkinsDto;
import ru.itis.skins_marketplace.models.Skin;

public interface TradeService {

    Skin tradeUserSkinsForOneHigherRareSkin(Long userId, TradeSkinsDto dto);
}
