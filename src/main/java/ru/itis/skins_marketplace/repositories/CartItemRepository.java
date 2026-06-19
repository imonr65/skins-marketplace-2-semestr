package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.skins_marketplace.models.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("""
    select ci from CartItem ci
    join fetch ci.user u
    where u.id = :userId
    order by ci.addedAt
    """)
    List<CartItem> getOrderedCartItemsByUserId(@Param("userId") Long userId);

    @Query("""
    select ci from CartItem ci
    join fetch ci.user u
    where ci = :cartItemId and u.id = :userId
    """)
    CartItem findAllByUserIdAndId(@Param("userId") Long userId, @Param("cartItemId") Long cartItemId);
}
