package ru.itis.skins_marketplace.fishki.second.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.skins_marketplace.exceptions.SkinNotFoundException;
import ru.itis.skins_marketplace.exceptions.UserNotFoundException;
import ru.itis.skins_marketplace.fishki.second.TradeSkinsDto;
import ru.itis.skins_marketplace.models.Skin;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.models.enums.ListingStatus;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.repositories.SkinRepository;
import ru.itis.skins_marketplace.repositories.SkinTemplateRepository;
import ru.itis.skins_marketplace.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final UserRepository userRepository;

    private final SkinRepository skinRepository;

    private final SkinTemplateRepository skinTemplateRepository;

    private static final int MIN_TRADE_COUNT = 5;
    private static final int MAX_TRADE_COUNT = 10;

    @Override
    public Skin tradeUserSkinsForOneHigherRareSkin(Long userId, TradeSkinsDto dto) {
        User user = userRepository.findByIdWithInventory(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Long> skinIds = dto.getSkinsId();

        if (skinIds == null || skinIds.size() < MIN_TRADE_COUNT || skinIds.size() > MAX_TRADE_COUNT) {
            throw new IllegalArgumentException("Неверное количество скинов. Нужно от " + MIN_TRADE_COUNT + " до " + MAX_TRADE_COUNT);
        }

        List<Skin> skinsToTrade = new ArrayList<>();
        Rarity commonRarity = null;

        for (Long skinId : skinIds) {
            Skin skin = skinRepository.findById(skinId)
                    .orElseThrow(() -> new SkinNotFoundException("Скин не найден: " + skinId));

            if (!skin.getUserInventory().getUser().getId().equals(userId)) {
                throw new SecurityException("Скин " + skinId + " не принадлежит текущему пользователю");
            }

            boolean hasActiveListing = skin.getListing().stream()
                    .anyMatch(l -> l.getListingStatus() == ListingStatus.ACTIVE);
            if (hasActiveListing) {
                throw new IllegalStateException("Скин " + skin.getTemplate().getName() + " выставлен на продажу. Снимите с продажи перед обменом.");
            }

            Rarity currentRarity = skin.getTemplate().getRarity();
            if (commonRarity == null) {
                commonRarity = currentRarity;
            } else if (commonRarity != currentRarity) {
                throw new IllegalArgumentException("Все скины должны иметь одинаковую редкость");
            }

            skinsToTrade.add(skin);
        }

        Rarity targetRarity = getNextRarity(commonRarity);
        if (targetRarity == null) {
            throw new IllegalStateException("Нельзя повысить редкость выше GOLDEN");
        }

        List<SkinTemplate> templates = skinTemplateRepository.findSkinTemplatesByRarity(targetRarity);
        if (templates.isEmpty()) {
            throw new RuntimeException("Нет доступных шаблонов для редкости " + targetRarity);
        }
        SkinTemplate randomTemplate = templates.get(new Random().nextInt(templates.size()));

        double randomFloat = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

        Skin newSkin = Skin.builder()
                .skinFloat(randomFloat)
                .template(randomTemplate)
                .userInventory(user.getUserInventory())
                .build();
        skinRepository.save(newSkin);

        for (Skin skin : skinsToTrade) {
            user.getUserInventory().getInventorySkins().remove(skin);
            skinRepository.delete(skin);
        }

        userRepository.save(user);


        return null;
    }

    private Rarity getNextRarity(Rarity current) {
        switch (current) {
            case Gray:    return Rarity.Blue;
            case Blue:    return Rarity.Pink;
            case Pink:    return Rarity.Red;
            case Red:     return Rarity.GOLDEN;
            case GOLDEN:  return null;
            default: throw new IllegalArgumentException("Неизвестная редкость: " + current);
        }
    }
}
