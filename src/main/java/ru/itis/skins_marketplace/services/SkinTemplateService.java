package ru.itis.skins_marketplace.services;

import org.springframework.data.domain.Page;
import ru.itis.skins_marketplace.dto.request.SkinTemplateDto;
import ru.itis.skins_marketplace.dto.request.SellingSkinTemplateDto;
import ru.itis.skins_marketplace.dto.request.SkinTemplateUpdateDto;
import ru.itis.skins_marketplace.models.SkinTemplate;

import java.util.List;

public interface SkinTemplateService {

    SkinTemplate add(SkinTemplateDto templateDto);

    Page<SkinTemplate> getSkinTemplatesByBatch(int page, int size);

    SkinTemplate findByWeaponCategoryAndTypeAndSlug(SellingSkinTemplateDto dto);

    List<SkinTemplate> getSkinTemplatesWithoutImageUrl();

    List<SkinTemplateDto> getAllSkinTemplates();

    SkinTemplate updateImage(SkinTemplate skinTemplate, String imageUrl);

    SkinTemplate update(Long skinTemplateId, SkinTemplateUpdateDto dto);

    SkinTemplate findById(Long skinTemplateId);

    void deleteById(Long skinTemplateId);
}
