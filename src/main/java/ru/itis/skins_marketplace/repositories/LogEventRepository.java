package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.skins_marketplace.models.LogEvent;
import ru.itis.skins_marketplace.models.enums.Type;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogEventRepository extends JpaRepository<LogEvent, UUID> {

    List<LogEvent> findAllByType(Type type);
}
