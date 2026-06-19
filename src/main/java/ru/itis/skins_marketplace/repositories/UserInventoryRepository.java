package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.models.UserInventory;

@RequestMapping
public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {
}
