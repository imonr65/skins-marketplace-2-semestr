package ru.itis.skins_marketplace.services;

import ru.itis.skins_marketplace.models.LogEvent;
import ru.itis.skins_marketplace.models.enums.Type;

import java.util.List;

public interface LogEventService {

    LogEvent save(LogEvent logEvent);

    List<LogEvent> getAllByType(Type type);
}
