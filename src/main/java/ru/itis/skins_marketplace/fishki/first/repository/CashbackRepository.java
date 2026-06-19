package ru.itis.skins_marketplace.fishki.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.skins_marketplace.fishki.first.model.CashbackSubscribe;
import ru.itis.skins_marketplace.fishki.first.model.SubscribeType;

import java.util.Optional;

public interface CashbackRepository extends JpaRepository<CashbackSubscribe, Long> {

    Optional<CashbackSubscribe> findBySubscribeType(SubscribeType type);
}
