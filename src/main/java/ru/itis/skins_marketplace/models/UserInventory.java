package ru.itis.skins_marketplace.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class UserInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @OneToMany(mappedBy = "userInventory", cascade = CascadeType.ALL)
    private Set<Skin> inventorySkins;

    @OneToOne(mappedBy = "userInventory")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserInventory inventory = (UserInventory) o;
        return Objects.equals(inventoryId, inventory.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(inventoryId);
    }
}
