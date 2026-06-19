package ru.itis.skins_marketplace.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.skins_marketplace.models.enums.skin.WEAR;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "skins")
public class Skin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double skinFloat;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private SkinTemplate template;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private UserInventory userInventory;

    @OneToMany(mappedBy = "skin", cascade = CascadeType.ALL)
    private List<Listing> listing = new ArrayList<>();

    public WEAR getWear() {
        if (skinFloat < 0.07) return WEAR.FACTORY_NEW;
        if (skinFloat < 0.15) return WEAR.MINIMAL_WEAR;
        if (skinFloat < 0.35) return WEAR.FIELD_TESTED;
        if (skinFloat < 0.45) return WEAR.WELL_WORN;
        return WEAR.BATTLE_SCARED;
    }

}
