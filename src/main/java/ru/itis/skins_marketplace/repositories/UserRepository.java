package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.models.enums.Role;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User u set u.role= :role where u.email = :email")
    void updateRoleByEmail(@Param("email") String email, @Param("role") Role role);

    @Query("select u from User u " +
            "left join fetch u.userInventory inv " +
            "left join fetch inv.inventorySkins " +
            "where u.id = :id")
    Optional<User> findByIdWithInventory(@Param("id") Long id);

    Optional<User> findByConfirmCode(String confirmCode);
}