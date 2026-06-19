package ru.itis.skins_marketplace.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.models.SkinTemplate;
import ru.itis.skins_marketplace.models.enums.ListingStatus;
import ru.itis.skins_marketplace.models.enums.skin.Rarity;
import ru.itis.skins_marketplace.models.enums.skin.WEAR;
import ru.itis.skins_marketplace.models.enums.skin.WeaponCategory;
import ru.itis.skins_marketplace.models.enums.skin.WeaponType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    @Query("select l from Listing l where l.skin.id = :skinId and l.listingStatus = 'ACTIVE'")
    Optional<Listing> findActiveListingBySkinId(@Param("skinId") Long skinId,
                                                @Param("status") ListingStatus status);

    boolean existsBySkinIdAndListingStatus(Long skinId, ListingStatus status);

    @Query("select l from Listing l where l.skin.id in :skinIds and l.listingStatus = :status")
    List<Listing> findActiveBySkinIds(@Param("skinIds") List<Long> skinIds,
                                      @Param("status") ListingStatus status);

    @Query("select l from Listing l " +
            "join fetch l.skin s " +
            "join fetch s.template " +
            "where l.seller.id = :sellerId and l.listingStatus = :status"
    )
    List<Listing> findActiveBySellerId(@Param("sellerId") Long sellerId,
                                       @Param("status") ListingStatus status);

    Page<Listing> findListingByListingStatus(ListingStatus status, Pageable pageable);

    @Query(value = """
    select l from Listing l
        join fetch l.skin s
        join fetch s.template st
            where l.listingStatus = :status and st.id = :skinTemplateId
    """)
    List<Listing> findAllOrderedByPriceListingsByListingStatus(@Param("status") ListingStatus status, Long skinTemplateId);

    @Query("""
    select l from Listing l
        join fetch l.skin s
        join fetch s.template sk
        where l.listingStatus = 'ACTIVE'
        and (:weaponCategory is null or sk.weaponCategory = :weaponCategory)
        and (:weaponType is null  or sk.weaponType = :weaponType)
        and (:rarity is null or sk.rarity = :rarity)
        and (:wear is null or sk.wear =: wear)
        and (:priceFrom is null or l.price >= :priceFrom)
        and (:priceTo is null or l.price <= :priceTo)
        and (:searchQuery is null or :searchQuery = '' or lower(sk.name) like lower(concat('%', :searchQuery, '%') ))
    """)
    List<Listing> findAllBySkinTemplateAndPriceBeforeAndAfter(
            @Param("weaponCategory") WeaponCategory weaponCategory,
            @Param("weaponType") WeaponType weaponType,
            @Param("rarity") Rarity rarity,
            @Param("wear") WEAR wear,
            @Param("priceFrom") BigDecimal priceFrom,
            @Param("priceTo") BigDecimal priceTo,
            @Param("searchQuery") String searchQuery
    );


}