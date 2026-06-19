package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.models.Skin;

public interface SkinService {

    void saveSkinToUser(Long userId, Long skinTemplateId, Double skinFloat);

    Skin findBySkinTemplateId(Long skinTemplateId);
}
