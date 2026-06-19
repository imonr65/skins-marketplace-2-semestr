    package ru.itis.skins_marketplace.services.impl;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import ru.itis.skins_marketplace.dto.ListingCreateDto;
    import ru.itis.skins_marketplace.dto.ListingDto;
    import ru.itis.skins_marketplace.dto.request.MarketFilterDto;
    import ru.itis.skins_marketplace.dto.response.ListingCardMarketDto;
    import ru.itis.skins_marketplace.exceptions.SkinNotFoundException;
    import ru.itis.skins_marketplace.exceptions.SkinTemplateNotFoundException;
    import ru.itis.skins_marketplace.exceptions.UserNotFoundException;
    import ru.itis.skins_marketplace.mappers.ListingEntityToListingCardMarketDto;
    import ru.itis.skins_marketplace.models.*;
    import ru.itis.skins_marketplace.models.enums.ListingStatus;
    import ru.itis.skins_marketplace.models.enums.Type;
    import ru.itis.skins_marketplace.models.enums.skin.Rarity;
    import ru.itis.skins_marketplace.models.enums.skin.WEAR;
    import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
    import ru.itis.skins_marketplace.models.enums.skin.WeaponType;
    import ru.itis.skins_marketplace.repositories.ListingRepository;
    import ru.itis.skins_marketplace.repositories.SkinRepository;
    import ru.itis.skins_marketplace.repositories.SkinTemplateRepository;
    import ru.itis.skins_marketplace.repositories.UserRepository;
    import ru.itis.skins_marketplace.services.ListingService;
    import ru.itis.skins_marketplace.services.LogEventService;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.time.OffsetDateTime;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import java.util.function.Function;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class ListingServiceImpl implements ListingService {

        private final ListingRepository listingRepository;
        private final SkinRepository skinRepository;
        private final SkinTemplateRepository skinTemplateRepository;
        private final UserRepository userRepository;
        private final LogEventService logEventService;
        private final ListingEntityToListingCardMarketDto mapper;

        @Override
        @Transactional
        public void createListing(Long sellerId, ListingCreateDto dto) {
            Skin skin = skinRepository.findById(dto.getSkinId()).orElseThrow(SkinNotFoundException::new);

            if (skin.getUserInventory() == null ||
                    !skin.getUserInventory().getUser().getId().equals(sellerId)) {
                logEventService.save(LogEvent.builder()
                        .text("Попытка продажи чужого скина")
                        .method(this.getClass().getSimpleName() + ".createListing(Long sellerId, ListingCreateDto dto)")
                        .createAt(OffsetDateTime.now())
                        .type(Type.ERROR)
                        .build());
                throw new IllegalArgumentException("Вы не можете выставить на продажу чужой скин!");
            }

            if (listingRepository.existsBySkinIdAndListingStatus(skin.getId(), ListingStatus.ACTIVE)) {
                logEventService.save(LogEvent.builder()
                        .text("Попытка продажи уже выстваленного скина")
                        .method(this.getClass().getSimpleName() + ".createListing(Long sellerId, ListingCreateDto dto)")
                        .createAt(OffsetDateTime.now())
                        .type(Type.ERROR)
                        .build());
                throw new IllegalStateException("Этот скин уже находится в активной продаже");
            }

            User seller = userRepository.findById(sellerId)
                    .orElseThrow(UserNotFoundException::new);

            Listing listing = Listing.builder()
                    .skin(skin)
                    .createdDate(LocalDateTime.now())
                    .listingStatus(ListingStatus.ACTIVE)
                    .seller(seller)
                    .price(dto.getPrice())
                    .viewsCount(0)
                    .build();
            listingRepository.save(listing);
            logEventService.save(LogEvent.builder()
                    .text("Скин " + skin + " выствлен на продажу пользователем " + seller.getName().toString() + " за " + dto.getPrice())
                    .method(this.getClass().getSimpleName() + ".createListing(Long sellerId, ListingCreateDto dto)")
                    .createAt(OffsetDateTime.now())
                    .type(Type.SUCCESS)
                    .build());
            log.info("Скин {} выставлен на продажу пользователем {} за {}",
                    skin, seller.getName(), dto.getPrice());
        }

        @Override
        public ListingDto createListingByAdmin(ListingDto dto) {
            log.debug("ListingService - метод createListingByAdmin - данные дто: {}", dto);
            User user = userRepository.findByEmail(dto.getUserEmail())
                    .orElseThrow(UserNotFoundException::new);
            SkinTemplate skinTemplate = skinTemplateRepository.findByName(dto.getSkinTemplateName())
                    .orElseThrow(SkinTemplateNotFoundException::new);
            Skin skin = Skin.builder()
                    .skinFloat(dto.getSkinFloat())
                    .userInventory(user.getUserInventory())
                    .template(skinTemplate)
                    .build();
            skinRepository.save(skin);
            Listing listing = Listing.builder()
                    .seller(user)
                    .skin(skin)
                    .viewsCount(0)
                    .price(BigDecimal.valueOf(dto.getPrice()))
                    .listingStatus(ListingStatus.ACTIVE)
                    .createdDate(LocalDateTime.now())
                    .build();
            Listing l = listingRepository.save(listing);
            dto.setId(l.getListingId());
            return dto;
        }

        @Override
        public List<Listing> getAllActiveOrderedListings(Long skinTemplateId) {
            return listingRepository.findAllOrderedByPriceListingsByListingStatus(ListingStatus.ACTIVE, skinTemplateId);
        }

        @Override
        @Transactional(readOnly = true)
        public List<Listing> getAll() {
            return listingRepository.findAll();
        }

        @Override
        @Transactional(readOnly = true)
        public Map<Long, Listing> getActiveListingsForSkinIds(List<Long> skinIds) {
            if (skinIds == null || skinIds.isEmpty()) {
                return Map.of();
            }
            List<Listing> activeListings = listingRepository.findActiveBySkinIds(skinIds, ListingStatus.ACTIVE);
            return activeListings.stream()
                    .collect(Collectors.toMap(l -> l.getSkin().getId(), Function.identity()));
        }

        @Override
        public List<Listing> getAllActiveListingsBySeller(Long sellerId) {
            return listingRepository.findActiveBySellerId(sellerId, ListingStatus.ACTIVE);
        }

        @Override
        public Page<ListingCardMarketDto> getAllLisingsByPage(int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            return listingRepository.findListingByListingStatus(ListingStatus.ACTIVE, pageable)
                    .map(mapper::toDto);
        }

        @Override
        public Listing findBySkinId(Long skinId) {
            return listingRepository.findActiveListingBySkinId(skinId, ListingStatus.ACTIVE)
                    .orElseThrow(SkinNotFoundException::new);
        }

        @Override
        public List<Listing> getListingsByFilter(MarketFilterDto dto) {
            SkinTemplate t = findSkinTemplatesByFilter(dto);
            return listingRepository.findAllBySkinTemplateAndPriceBeforeAndAfter(
                    t.getWeaponCategory(),
                    t.getWeaponType(),
                    t.getRarity(),
                    t.getWear(),
                    dto.getPriceTo(),
                    dto.getPriceFrom(),
                    dto.getSearchQuery()
            );
        }

        @Override
        public void updateListingPrice(Long listingId, BigDecimal newPrice, Long sellerId) {
            Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Нет записи о продажи"));

            if(!listing.getSeller().getId().equals(sellerId)) {
                throw new SecurityException("Вы не можете изменять цену чужого объявления");
            }
            listing.setPrice(newPrice);
            listingRepository.save(listing);
        }

        @Override
        public void deleteListing(Long listingId, Long sellerId) {
            Optional<Listing> optionalListing = listingRepository.findById(listingId);
            if (optionalListing.isEmpty()) {
                throw new RuntimeException();
            }
            Listing listing = optionalListing.get();
            if(!listing.getSeller().getId().equals(sellerId)) {
                throw new SecurityException("Вы не можете удалять чужое объявление");
            }

            listingRepository.delete(listing);
        }


        private SkinTemplate findSkinTemplatesByFilter(MarketFilterDto dto) {
            log.debug("MarketFilterDto: {}", dto);
            SkinTemplate skinTemplate = new SkinTemplate();
            if (dto.getWeaponCategory() != null && !dto.getWeaponCategory().isBlank()) {
                skinTemplate.setWeaponCategory(WeaponCategory.valueOf(dto.getWeaponCategory()));
            }

            if (dto.getWeaponType() != null  && !dto.getWeaponType().isBlank()) {
                skinTemplate.setWeaponType(WeaponType.valueOf(dto.getWeaponType()));
            }

            if (dto.getWear() != null && !dto.getWear().isBlank()) {
                skinTemplate.setWear(WEAR.valueOf(dto.getWear()));
            }

            if (dto.getRarity() != null && !dto.getRarity().isBlank()) {
                skinTemplate.setRarity(Rarity.valueOf(dto.getRarity()));
            }
            return skinTemplate;
        }

    }