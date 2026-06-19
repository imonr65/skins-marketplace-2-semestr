package ru.itis.skins_marketplace.controllers.restApi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.skins_marketplace.dto.request.SkinTemplateDto;
import ru.itis.skins_marketplace.dto.request.SkinTemplateUpdateDto;
import ru.itis.skins_marketplace.mappers.SkinTemplateEntityToDtoMapper;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.services.SkinTemplateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skin-templates")
public class SkinTemplateRestApi {

    private final SkinTemplateService skinTemplateService;

    private final SkinTemplateEntityToDtoMapper mapper;

    @GetMapping
    public List<SkinTemplateDto> getAllSkinTemplates() {
        return skinTemplateService.getAllSkinTemplates();
    }

    @PostMapping
    public SkinTemplateDto createSkinTemplate(@RequestBody SkinTemplateDto dto) {
        return mapper.toDto(skinTemplateService.add(dto));
    }

    @PutMapping("/{id}")
    public SkinTemplateDto updateSkinTemplate(@PathVariable Long id, @RequestBody SkinTemplateUpdateDto dto) {
        SkinTemplate skinTemplate = skinTemplateService.update(id, dto);
        return mapper.toDto(skinTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkinTemplateById(@PathVariable Long id) {
        skinTemplateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
