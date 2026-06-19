package ru.itis.skins_marketplace.fishki.first.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.skins_marketplace.exceptions.UserHasNotEnoughMoneyOnBalanceException;
import ru.itis.skins_marketplace.fishki.first.dto.CashbackSubscribeRequest;
import ru.itis.skins_marketplace.fishki.first.dto.CashbackSubscribeResponse;
import ru.itis.skins_marketplace.fishki.first.model.CashbackSubscribe;
import ru.itis.skins_marketplace.fishki.first.model.SubscribeType;
import ru.itis.skins_marketplace.fishki.first.repository.CashbackRepository;
import ru.itis.skins_marketplace.models.User;
import ru.itis.skins_marketplace.services.UserService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CashbackServiceImpl implements CashbackService {

    private final UserService userService;

    private final CashbackRepository cashbackRepository;

    @Override
    public BigDecimal getUserCashback(Long userId, BigDecimal priceForPurchase) {
        User user = userService.findById(userId);
        BigDecimal cashback = priceForPurchase
                .multiply(getPercentageOfCashbackSubscribe(user.getCashbackSubscribe().getSubscribeType()));
        return cashback;
    }

    @Override
    public List<CashbackSubscribeResponse> getAllSubscribes() {
        List<SubscribeType> types = SubscribeType.getNonWithout();
        List<CashbackSubscribeResponse> dtos = new ArrayList<>();
        for (SubscribeType type: types) {
            dtos.add(CashbackSubscribeResponse.builder()
                            .price(type.getPrice())
                            .typeName(type.name())
                            .typeName(type.name()).build());
        }
        return dtos;
    }

    @Override
    @Transactional
    public CashbackSubscribe userSubscribed(Long userId, CashbackSubscribeRequest dto) {
        User user = userService.findById(userId);
        SubscribeType type = SubscribeType.valueOf(dto.getTypeName());
        BigDecimal price = type.getPrice().multiply(BigDecimal.valueOf(dto.getDuration()));

        if (user.getBalance().compareTo(price) < 0) {
            throw new UserHasNotEnoughMoneyOnBalanceException();
        }

        CashbackSubscribe cashbackSubscribe = CashbackSubscribe.builder()
                .price(price)
                .subscribeType(type)
                .subscriptionStartTime(OffsetDateTime.now())
                .subscriptionEndTime(OffsetDateTime.now().plusMonths(dto.getDuration()))
                .build();
        cashbackRepository.save(cashbackSubscribe);

        user.setCashbackSubscribe(cashbackSubscribe);
        user.setBalance(user.getBalance().subtract(price));
        userService.update(user);
        return cashbackSubscribe;
    }

    @Override
    public void deleteByType(Long userId, CashbackSubscribeRequest dto) {
        User user = userService.findById(userId);
        cashbackRepository.delete(user.getCashbackSubscribe());
        SubscribeType type = SubscribeType.valueOf(dto.getTypeName());
        BigDecimal price = type.getPrice().multiply(BigDecimal.valueOf(dto.getDuration()));
        CashbackSubscribe cashbackSubscribe = CashbackSubscribe.builder()
                .price(price)
                .subscribeType(type)
                .subscriptionStartTime(OffsetDateTime.now())
                .subscriptionEndTime(OffsetDateTime.now().plusMonths(dto.getDuration()))
                .build();
        user.setCashbackSubscribe(cashbackSubscribe);
        userService.update(user);
    }

    private BigDecimal getPercentageOfCashbackSubscribe(SubscribeType subscribeType) {
        switch (subscribeType) {
            case WITHOUT -> {return BigDecimal.valueOf(0.0);}
            case COMMON -> {return  BigDecimal.valueOf(0.03);}
            case SILVER -> {return  BigDecimal.valueOf(0.05);}
            case GOLD -> {return  BigDecimal.valueOf(0.1);}
        }
        return BigDecimal.valueOf(0);
    }
}
