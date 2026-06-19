package ru.itis.skins_marketplace.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import ru.itis.skins_marketplace.fishki.first.model.CashbackSubscribe;
import ru.itis.skins_marketplace.models.enums.Role;

import java.math.BigDecimal;
import java.util.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String imageUrl;

    @DecimalMin(value = "0.0")
    @Column(nullable = false, precision = 10, scale = 2,
            columnDefinition = "decimal(10,2) check(balance >= 0)"
    )
    private BigDecimal balance;

    private String confirmed;

    private String confirmCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cashback_subscribe_id")
    private CashbackSubscribe cashbackSubscribe;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private UserInventory userInventory;

    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skin_template_id")
    )
    private Set<SkinTemplate> favoritesSkins = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
