package ru.itis.skins_marketplace.fishki.first.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Cashback_subscribes")
public class CashbackSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubscribeType subscribeType;

    private BigDecimal price;

    private OffsetDateTime subscriptionStartTime;

    private OffsetDateTime subscriptionEndTime;
}
