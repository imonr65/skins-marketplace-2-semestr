package ru.itis.skins_marketplace.services.impl;

import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.skins_marketplace.dto.request.SkinTemplateDto;
import ru.itis.skins_marketplace.dto.request.SellingSkinTemplateDto;
import ru.itis.skins_marketplace.dto.request.SkinTemplateUpdateDto;
import ru.itis.skins_marketplace.exceptions.SkinTemplateAlreadyExistsException;
import ru.itis.skins_marketplace.exceptions.SkinTemplateNotFoundException;
import ru.itis.skins_marketplace.mappers.SkinTemplateEntityToDtoMapper;
import ru.itis.skins_marketplace.models.LogEvent;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.enums.Type;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WEAR;
import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;
import ru.itis.skins_marketplace.repositories.SkinTemplateRepository;
import ru.itis.skins_marketplace.services.LogEventService;
import ru.itis.skins_marketplace.services.SkinTemplateService;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class SkinTemplateServiceImpl implements SkinTemplateService {

    private final SkinTemplateRepository templateRepository;

    private final SkinTemplateEntityToDtoMapper mapper;

    private final Slugify slugify = Slugify.builder().build();

    private final LogEventService logEventService;

    @Override
    public SkinTemplate add(SkinTemplateDto templateDto) {
        log.debug("Шаблон скина дто: имя -{},категория - {}, редкость - {}, поношенность - {}, тип оружия - {}",
                templateDto.getName(), templateDto.getWeaponCategory(), templateDto.getRarity(), templateDto.getWear(), templateDto.getWeaponType());
        String name = templateDto.getName();
        WeaponType weaponType;
        Rarity rarity;
        WEAR wear;
        WeaponCategory weaponCategory;
        try {
            weaponCategory = WeaponCategory.valueOf(templateDto.getWeaponCategory());
            weaponType = WeaponType.valueOf(templateDto.getWeaponType());
            rarity = Rarity.valueOf(templateDto.getRarity());
            wear = WEAR.valueOf(templateDto.getWear());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value in DTO", e);
        }
        checkOnExists(name, rarity, wear, weaponType);

        String slug = slugify.slugify(name + wear.name());

        SkinTemplate skinTemplate = SkinTemplate.builder()
                .name(name)
                .rarity(rarity)
                .weaponCategory(weaponCategory)
                .wear(wear)
                .weaponType(weaponType)
                .slug(slug)
                .imageUrl(templateDto.getImageUrl())
                .build();
        logEventService.save(LogEvent.builder()
                        .text("Успешное сохранение шаблона скина")
                        .method(this.getClass().getSimpleName() + ".add(SkinTemplateDto templateDto)")
                        .createAt(OffsetDateTime.now())
                        .type(Type.SUCCESS)
                .build());
        return templateRepository.save(skinTemplate);

    }

    @Override
    public SkinTemplate findByWeaponCategoryAndTypeAndSlug(SellingSkinTemplateDto dto) {
        log.debug("SellingSkinTemplateDto: {}", dto);
        if (dto.getSlug() == null || dto.getWeaponType() == null || dto.getWeaponCategory() == null) {
            throw new IllegalArgumentException();
        }

        WeaponCategory category = WeaponCategory.valueOf(dto.getWeaponCategory());
        WeaponType weaponType = WeaponType.valueOf(dto.getWeaponType());
        String slug = dto.getSlug();

        return templateRepository.findByWeaponCategoryAndWeaponTypeAndSlug(category, weaponType, slug)
                .orElseThrow(SkinTemplateNotFoundException::new);
    }

    @Override
    public List<SkinTemplate> getSkinTemplatesWithoutImageUrl() {
        return templateRepository.findSkinTemplatesWithoutImage();
    }

    @Override
    public List<SkinTemplateDto> getAllSkinTemplates() {
        return templateRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public SkinTemplate updateImage(SkinTemplate entity, String imageUrl) {
        entity.setImageUrl(imageUrl);
        return templateRepository.save(entity);
    }

    @Override
    public SkinTemplate update(Long skinTemplateId, SkinTemplateUpdateDto dto) {
        SkinTemplate skinTemplate = templateRepository
                .findById(skinTemplateId).orElseThrow(SkinTemplateNotFoundException::new);

        skinTemplateChanges(skinTemplate, dto);

        return templateRepository.save(skinTemplate);
    }

    @Override
    public SkinTemplate findById(Long skinTemplateId) {
        return templateRepository.findById(skinTemplateId).orElseThrow(SkinTemplateNotFoundException::new);
    }

    @Override
    public void deleteById(Long skinTemplateId) {
        templateRepository.deleteById(skinTemplateId);
    }


    @Override
    public Page<SkinTemplate> getSkinTemplatesByBatch(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return templateRepository.findAll(pageable);
    }

    private void checkOnExists(String name, Rarity rarity, WEAR wear, WeaponType weaponType) {
        if (templateRepository.existsByNameAndRarityAndWearAndWeaponType(name, rarity, wear, weaponType)) {
            throw new SkinTemplateAlreadyExistsException(
                    String.format("Шаблог скина с именем %s," +
                                    " редкостью %s," +
                                    " поношенностью %s" +
                                    " и типом оружия %s уже существует",
                            name, rarity, wear, weaponType
                    )
            );
        }
    }

    private void skinTemplateChanges(SkinTemplate skinTemplate, SkinTemplateUpdateDto dto) {
        if (dto.getName() != null) {
            skinTemplate.setName(dto.getName());
        }
        if (dto.getWear() != null) {
            skinTemplate.setWear(WEAR.valueOf(dto.getWear()));
        }
        if (dto.getRarity() != null) {
            skinTemplate.setRarity(Rarity.valueOf(dto.getRarity()));
        }
        if (dto.getWeaponType() != null) {
            skinTemplate.setWeaponType(WeaponType.valueOf(dto.getWeaponType()));
        }
        if (dto.getWeaponCategory() != null) {
            skinTemplate.setWeaponCategory(WeaponCategory.valueOf(dto.getWeaponCategory()));
        }

        String slug = slugify.slugify(skinTemplate.getName() + skinTemplate.getWear());
        skinTemplate.setSlug(slug);
    }

}
