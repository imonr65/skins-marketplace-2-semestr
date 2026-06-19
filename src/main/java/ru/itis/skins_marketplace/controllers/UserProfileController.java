package ru.itis.skins_marketplace.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.skins_marketplace.dto.ListingCreateDto;
import ru.itis.skins_marketplace.dto.users.UserProfileDto;
import ru.itis.skins_marketplace.mappers.user.UserEntityToProfileDto;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.models.Skin;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.security.CustomUserDetailsImpl;
import ru.itis.skins_marketplace.services.FileStorageService;
import ru.itis.skins_marketplace.services.ListingService;
import ru.itis.skins_marketplace.services.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserService userService;

    private final UserEntityToProfileDto profileDtoMapper;

    private final ListingService listingService;

    private final FileStorageService fileStorageService;

    @GetMapping()
    public String getUserProfilePage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        log.debug("Заходим на страницу профиля");
        User user = userService.findByIdWithInventory(userDetails.getUser().getId());
        Set<Skin> inventorySkins = user.getUserInventory().getInventorySkins();
        List<Long> skinIds = inventorySkins.stream()
                .map(Skin::getId)
                .toList();

        Map<Long, Listing> listings = listingService.getActiveListingsForSkinIds(skinIds);
        Map<String, Listing> activeListings = listings.entrySet().stream()
                        .collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));

        model.addAttribute("user", profileDtoMapper.toDto(user));
        model.addAttribute("userInventory", user.getUserInventory());
        model.addAttribute("inventory", user.getUserInventory().getInventorySkins());
        model.addAttribute("activeListings", activeListings);
        return "user_profile_page";
    }

    @GetMapping("/update-profile")
    public String getUpdateProfilePage(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
        log.debug("/update-profile - GET - Id пользователя: {}", userDetails.getUser().getId());
        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("user", profileDtoMapper.toDto(user));
        log.debug("/update-profile - GET - отработал успешно");
        return "user_edit_profile_page";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, @Valid UserProfileDto userProfileDto) {
        log.debug("/update-profile - POST - Id пользователя: {}", userDetails.getUser().getId());
        userService.updateProfile(userDetails.getUser().getId(), userProfileDto);
        return "redirect:/profile";
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("avatar") MultipartFile avatar,
                                          @AuthenticationPrincipal CustomUserDetailsImpl userDetails) {
        if (avatar == null || avatar.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Файл не выбран"));
        }

        User user = userService.findById(userDetails.getUser().getId());
        String avatarUrl = fileStorageService.saveAvatar(avatar);

        user.setImageUrl(avatarUrl);
        userService.update(user);
        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }

    @PostMapping("/skin/{skinId}/list")
    public String createListing(@AuthenticationPrincipal CustomUserDetailsImpl userDetails,
                                @PathVariable Long skinId,
                                @RequestParam BigDecimal price) {
        ListingCreateDto dto = ListingCreateDto.builder()
                .skinId(skinId)
                .price(price)
                .build();
        listingService.createListing(userDetails.getUser().getId(), dto);
        return "redirect:/profile";
    }
}
