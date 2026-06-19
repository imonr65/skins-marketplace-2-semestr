package ru.itis.skins_marketplace.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.request.SkinTemplateDto;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WEAR;
import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;
import ru.itis.skins_marketplace.services.SkinTemplateService;

@Slf4j
@Controller
@RequestMapping("/admin/skin-templates")
@AllArgsConstructor
public class AdminSkinTemplateController {

    private final SkinTemplateService skinTemplateService;

    @GetMapping("/create")
    public String getSkinTemplateServicePage(Model model) {
        model.addAttribute("weaponCategories", WeaponCategory.values());
        model.addAttribute("weaponTypes", WeaponType.values());
        model.addAttribute("rarities", Rarity.values());
        model.addAttribute("wears", WEAR.values());
        return "adminPages/skin_template_service";
    }

    @PostMapping("/create")
    public ResponseEntity<SkinTemplate> addSkinTemplate(@RequestBody SkinTemplateDto templateDto) {
        log.debug("SkinTemplateDto: {}", templateDto);
        SkinTemplate skinTemplate = skinTemplateService.add(templateDto);
        return ResponseEntity.ok(skinTemplate);
    }

}
