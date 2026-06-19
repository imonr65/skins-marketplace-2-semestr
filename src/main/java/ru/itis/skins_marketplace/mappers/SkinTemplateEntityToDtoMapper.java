package ru.itis.skins_marketplace.mappers;

import org.springframework.stereotype.Component;
import ru.itis.skins_marketplace.dto.request.SkinTemplateDto;
import ru.itis.skins_marketplace.models.SkinTemplate;

@Component
public class SkinTemplateEntityToDtoMapper {

    public SkinTemplateDto toDto(SkinTemplate skinTemplate) {
        return SkinTemplateDto.builder()
                .name(skinTemplate.getName())
                .weaponCategory(skinTemplate.getWeaponCategory().name())
                .weaponType(skinTemplate.getWeaponType().name())
                .rarity(skinTemplate.getRarity().name())
                .imageUrl(skinTemplate.getImageUrl())
                .wear(skinTemplate.getWear().getWearName())
                .build();
    }
}
