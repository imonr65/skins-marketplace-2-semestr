package ru.itis.skins_marketplace.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WEAR;
import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkinTemplateRepository extends JpaRepository<SkinTemplate, Long> {

    Optional<SkinTemplate> findByName(String name);

    Optional<SkinTemplate> findSkinTemplateByNameAndRarityAndWeaponType(String name, Rarity rarity, WeaponType weaponType);

    boolean existsByNameAndRarityAndWearAndWeaponType(String name, Rarity rarity, WEAR wear, WeaponType weaponType);

    Optional<SkinTemplate> findByWeaponCategoryAndWeaponTypeAndSlug(WeaponCategory weaponCategory, WeaponType type, String slug);

    @Query(value = """
    select * from skin_templates
    where image_url is null or image_url = ''
    """, nativeQuery = true)
    List<SkinTemplate> findSkinTemplatesWithoutImage();

    List<SkinTemplate> findSkinTemplatesByRarity(Rarity rarity);

}
