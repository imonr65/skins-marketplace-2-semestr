package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.skins_marketplace.models.Skin;
import ru.itis.skins_marketplace.models.SkinTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Long> {

    Optional<Skin> findByTemplateId(Long skinTemplateId);

}
