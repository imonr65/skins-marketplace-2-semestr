package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.skins_marketplace.models.Listing;
import ru.itis.skins_marketplace.models.PurchasedItem;

import java.util.List;

@Repository
public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {

    boolean existsPurchasedItemByListing(Listing listing);

    @Query("""
       select pi
       from PurchasedItem pi
       join fetch pi.listing l
       join fetch l.skin s
       join fetch s.template t
       join fetch pi.transaction tr
       where t.id = :skinTemplateId
       order by tr.completionDate asc
       """)
    List<PurchasedItem> findPriceHistoryBySkinTemplateId(Long skinTemplateId);

}
