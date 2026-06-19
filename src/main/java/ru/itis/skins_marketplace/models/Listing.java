package ru.itis.skins_marketplace.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.springframework.web.servlet.DispatcherServlet;
import ru.itis.skins_marketplace.models.enums.ListingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingId;

    @DecimalMin(value = "0.0")
    @Column(nullable = false, precision = 10, scale = 2,
            columnDefinition = "decimal(10,2) check (price >= 0)"
    )
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingStatus listingStatus = ListingStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private Integer viewsCount = 0;

    private LocalDateTime reservedUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", nullable = false)
    private Skin skin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToOne(mappedBy = "listing")
    private PurchasedItem purchasedItem;
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return Objects.equals(listingId, listing.listingId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(listingId);
    }

}
