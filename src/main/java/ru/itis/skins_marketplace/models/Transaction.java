package ru.itis.skins_marketplace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.skins_marketplace.models.enums.PaymentMethod;
import ru.itis.skins_marketplace.models.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private OffsetDateTime completionDate;

    @Column(nullable = false)
    private String paymentDetails;

    @ManyToOne
    @JoinColumn(
            name = "buyer_id",
            nullable = false
    )
    private User buyer;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<PurchasedItem> purchasedItems = new ArrayList<>();

}
