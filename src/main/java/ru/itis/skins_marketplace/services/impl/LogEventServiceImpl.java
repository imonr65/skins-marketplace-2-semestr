package ru.itis.skins_marketplace.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.skins_marketplace.models.LogEvent;
import ru.itis.skins_marketplace.models.enums.Type;
import ru.itis.skins_marketplace.repositories.LogEventRepository;
import ru.itis.skins_marketplace.services.LogEventService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogEventServiceImpl implements LogEventService {

    private final LogEventRepository logEventRepository;

    @Override
    public LogEvent save(LogEvent logEvent) {
        return logEventRepository.save(logEvent);
    }

    @Override
    public List<LogEvent> getAllByType(Type type) {
        return logEventRepository.findAllByType(type);
    }
}
