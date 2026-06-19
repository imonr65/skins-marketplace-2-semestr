package ru.itis.skins_marketplace.controllers.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.users.UserDto;
import ru.itis.skins_marketplace.dto.users.UserRoleForm;
import ru.itis.skins_marketplace.dto.users.UserSkinDto;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;
import ru.itis.skins_marketplace.services.UserService;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/changeRole")
    public String getChangeUserPage() {
        return "adminPages/users/change_user_role_page";
    }

    @GetMapping("createUser")
    public String getCreateUserPage() {
        return "adminPages/users/create_user_page";
    }

    @PostMapping("/changeRole")
    public ResponseEntity<?> changeUser(UserRoleForm form) {
        userService.changeRole(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid UserDto userDto) {
        UserDto user = userService.save(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/giveSkins")
    private String getGiveSkinsPage(Model model) {
        model.addAttribute("weaponCategories", WeaponCategory.values());
        model.addAttribute("weaponTypes", WeaponType.values());
        model.addAttribute("rarities", Rarity.values());
        return "adminPages/users/give_skin_to_user_page";
    }

    @PostMapping("/giveSkins")
    public ResponseEntity<UserSkinDto> giveSkins(@Valid UserSkinDto dto) {
        log.debug("UserSkinDto информация: {}", dto);
        UserSkinDto skinDto = userService.saveUserWithSkin(dto);
        return ResponseEntity.ok(skinDto);
    }


}
