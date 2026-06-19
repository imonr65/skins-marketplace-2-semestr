package ru.itis.skins_marketplace.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itis.skins_marketplace.dto.api.ApiSkinDto;
import ru.itis.skins_marketplace.dto.api.SkinInfo;
import ru.itis.skins_marketplace.mappers.SkinTemplateEntityToDtoMapper;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.services.SkinTemplateService;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiSkinImageScheduler {

    private final RestTemplate restTemplate;

    @Value("${clients.skins-json-url}")
    private String skinsJsonUrl;

    private final SkinTemplateService skinTemplateService;


    @Scheduled(initialDelay = 0, fixedDelay = 20_000)
    public void processSkinTemplatesImages() throws JsonProcessingException {
        log.debug("Работа ApiSkinImageScheduler-а");
        List<SkinTemplate> skinTemplatesWithoutImageUrl = skinTemplateService.getSkinTemplatesWithoutImageUrl();
        log.debug("Шаблоны: {}", skinTemplatesWithoutImageUrl);
        if (skinTemplatesWithoutImageUrl.isEmpty()) {
            return;
        }

        String json = restTemplate.getForObject(skinsJsonUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ApiSkinDto apiSkinDto = objectMapper.readValue(json, ApiSkinDto.class);

        if (apiSkinDto == null || apiSkinDto.skinInfoMap() == null) {
            throw new RuntimeException();
        }
        Map<String, SkinInfo> skinInfoMap = apiSkinDto.skinInfoMap();
        for (SkinTemplate skinTemplate: skinTemplatesWithoutImageUrl) {
            log.debug("Обработка шаблоноа с без imageUrl: {}", skinTemplate.getName());
            String skinTemplateName = skinTemplate.getName();

            String matchedKey = skinInfoMap.keySet().stream()
                    .filter(key -> key.contains(skinTemplateName))
                    .findFirst()
                    .orElse(null);
            if (matchedKey != null) {
                SkinInfo skinInfo = skinInfoMap.get(matchedKey);
                if (skinInfo.imageUrl() != null) {
                    log.debug("Установка imageUrl для skinTemplate: {}", skinTemplate);
                    skinTemplateService.updateImage(skinTemplate, skinInfo.imageUrl());
                }
            }

        }
    }
}
