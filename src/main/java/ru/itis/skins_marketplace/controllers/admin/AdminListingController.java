package ru.itis.skins_marketplace.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.skins_marketplace.dto.ListingDto;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.services.ListingService;

import java.util.List;

@Controller
@RequestMapping("/admin/listings")
@RequiredArgsConstructor
public class AdminListingController {

    private final ListingService listingService;

    @GetMapping("/list")
    public String getAllListings(Model model) {
        List<Listing> listings = listingService.getAll();
        model.addAttribute("listings", listings);
        return "adminPages/admin_listings_page";
    }

    @PostMapping("/create-listing")
    public ResponseEntity<?> createListing(@Valid ListingDto listingDto) {
        ListingDto dto = listingService.createListingByAdmin(listingDto);
        return ResponseEntity.ok(dto);
    }
}
