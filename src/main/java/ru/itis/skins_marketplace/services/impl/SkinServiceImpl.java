package ru.itis.skins_marketplace.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.skins_marketplace.exceptions.SkinNotFoundException;
import ru.itis.skins_marketplace.exceptions.SkinTemplateNotFoundException;
import ru.itis.skins_marketplace.exceptions.UserNotFoundException;
import ru.itis.skins_marketplace.models.Skin;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.repositories.SkinRepository;
import ru.itis.skins_marketplace.repositories.SkinTemplateRepository;
import ru.itis.skins_marketplace.repositories.UserRepository;
import ru.itis.skins_marketplace.services.SkinService;

@Service
@AllArgsConstructor
public class SkinServiceImpl implements SkinService {

    private final SkinRepository skinRepository;

    private final UserRepository userRepository;

    private final SkinTemplateRepository skinTemplateRepository;

    @Override
    public void saveSkinToUser(Long userId, Long skinTemplateId, Double skinFloat) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findByIdWithInventory(userId).orElseThrow(UserNotFoundException::new);
        SkinTemplate skinTemplate = skinTemplateRepository.findById(skinTemplateId).orElseThrow(SkinTemplateNotFoundException::new);
        Skin skin = Skin.builder()
                .userInventory(user.getUserInventory())
                .template(skinTemplate)
                .build();
        skinRepository.save(skin);
    }

    @Override
    public Skin findBySkinTemplateId(Long skinTemplateId) {
        return skinRepository.findByTemplateId(skinTemplateId).orElseThrow(SkinNotFoundException::new);
    }
}
