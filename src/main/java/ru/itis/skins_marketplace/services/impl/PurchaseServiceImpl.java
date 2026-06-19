package ru.itis.skins_marketplace.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.skins_marketplace.dto.request.SellingListingHistoryDto;
import ru.itis.skins_marketplace.exceptions.*;
import ru.itis.skins_marketplace.fishki.first.model.SubscribeType;
import ru.itis.skins_marketplace.fishki.first.services.CashbackService;
import ru.itis.skins_marketplace.models.*;
import ru.itis.skins_marketplace.models.enums.ListingStatus;
import ru.itis.skins_marketplace.models.enums.PaymentMethod;
import ru.itis.skins_marketplace.models.enums.TransactionStatus;
import ru.itis.skins_marketplace.models.enums.Type;
import ru.itis.skins_marketplace.repositories.ListingRepository;
import ru.itis.skins_marketplace.repositories.PurchasedItemRepository;
import ru.itis.skins_marketplace.repositories.TransactionRepository;
import ru.itis.skins_marketplace.repositories.UserRepository;
import ru.itis.skins_marketplace.services.LogEventService;
import ru.itis.skins_marketplace.services.PurchaseService;
import ru.itis.skins_marketplace.services.UserService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final ListingRepository listingRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final PurchasedItemRepository purchasedItemRepository;

    private final LogEventService logEventService;

    private final CashbackService cashbackService;

    private final UserService userService;

    @Override
    @Transactional
    public void quickPurchase(Long listingId, Long userId) {
        User buyer = userRepository.findByIdWithInventory(userId).orElseThrow(UserNotFoundException::new);
        Listing listing = listingRepository.findById(listingId).orElseThrow(ListingNotFoundException::new);

        validateListingAndUser(listing, buyer);

        makeTransaction(buyer, listing);
        giveUserCashback(buyer, listing.getPrice());
        logEventService.save(LogEvent.builder()
                .text("Успешная быстра покупка товара")
                .method(this.getClass().getSimpleName() + ".quickPurchase(Long listingId, Long userId)")
                .createAt(OffsetDateTime.now())
                .type(Type.SUCCESS)
                .build());
    }

    @Override
    @Transactional
    public void cartPurchases(Long userId, List<CartItem> cartItems) {
        User buyer = userRepository.findByIdWithInventory(userId).orElseThrow(UserNotFoundException::new);
        List<Listing> listings = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem: cartItems) {
            Listing listing = cartItem.getListing();
            validateListing(listing);
            listings.add(listing);
            totalPrice = totalPrice.add(listing.getPrice());
        }

        validateUserBalance(buyer, totalPrice);

        Transaction transaction = Transaction.builder()
                .buyer(buyer)
                .transactionStatus(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethod.BALANCE)
                .totalAmount(totalPrice)
                .paymentDetails("Fast purchase")
                .completionDate(OffsetDateTime.now())
                .build();
        List<PurchasedItem> items = new ArrayList<>();
        for (Listing listing: listings) {
            items.add(PurchasedItem.builder()
                    .transaction(transaction)
                    .listing(listing)
                    .priceAtPurchase(listing.getPrice())
                    .build()
            );
            makeTrade(buyer, listing, transaction);
            listingRepository.save(listing);
        }

        giveUserCashback(buyer, totalPrice);

        transaction.setPurchasedItems(items);
        transactionRepository.save(transaction);
        logEventService.save(LogEvent.builder()
                        .text("Успешная покупка товаров через корзину")
                        .method(this.getClass().getSimpleName() + ".cartPurchases(Long userId, List<CartItem> cartItems)")
                        .createAt(OffsetDateTime.now())
                        .type(Type.SUCCESS)
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public List<SellingListingHistoryDto> getPriceHistory(Long skinTemplateId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return purchasedItemRepository
                .findPriceHistoryBySkinTemplateId(skinTemplateId)
                .stream()
                .map(item -> SellingListingHistoryDto.builder()
                        .price(item.getPriceAtPurchase())
                        .soldAt(item.getTransaction().getCompletionDate())
                        .soldAtStr(item.getTransaction().getCompletionDate().format(formatter))
                        .build())
                .toList();
    }

    private void makeTrade(User buyer, Listing listing, Transaction transaction) {
        Skin purchaseSkin = listing.getSkin();
        User seller = userService.findByIdWithInventory(listing.getSeller().getId());

        seller.getUserInventory().getInventorySkins().remove(purchaseSkin);
        buyer.getUserInventory().getInventorySkins().add(purchaseSkin);

        purchaseSkin.setUserInventory(buyer.getUserInventory());

        buyer.setBalance(buyer.getBalance().subtract(listing.getPrice()));
        seller.setBalance(seller.getBalance().add(listing.getPrice()));

        listing.setListingStatus(ListingStatus.SOLD);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        userRepository.save(buyer);
        userRepository.save(seller);
    }


    private void makeTransaction(User buyer, Listing listing) {
        List<PurchasedItem> purchasedItems = new ArrayList<>();

        Transaction transaction = Transaction.builder()
                .buyer(buyer)
                .transactionStatus(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethod.BALANCE)
                .totalAmount(listing.getPrice())
                .paymentDetails("Fast purchase")
                .completionDate(OffsetDateTime.now())
                .purchasedItems(purchasedItems)
                .build();

        PurchasedItem item = PurchasedItem.builder()
                .transaction(transaction)
                .listing(listing)
                .priceAtPurchase(listing.getPrice())
                .build();

        makeTrade(buyer, listing, transaction);

        listingRepository.save(listing);
        purchasedItems.add(item);
        transactionRepository.save(transaction);
    }

    private void validateListingAndUser(Listing listing, User user) {
        if (listing.getSeller().getId().equals(user.getId())) {
            throw new SelfPurchaseException();
        }
        if (user.getBalance().compareTo(listing.getPrice()) < 0) {
            //todo:добавить в controllerAdvice
            throw new UserHasNotEnoughMoneyOnBalanceException();
        }
        validateListing(listing);
    }
    private void validateListing(Listing listing) {
        if (listing.getListingStatus() != ListingStatus.ACTIVE) {
            throw new ListingNotAvailableException();
        }
        if (purchasedItemRepository.existsPurchasedItemByListing(listing)) {
            throw new RuntimeException("Покупка такого объявления уже была");
        }
    }

    private void giveUserCashback(User buyer, BigDecimal purchasePrice) {
        if (buyer.getCashbackSubscribe().getSubscribeType() != SubscribeType.WITHOUT) {
            BigDecimal cashbackAmount = cashbackService.getUserCashback(buyer.getId(), purchasePrice);
            buyer.setBalance(buyer.getBalance().add(cashbackAmount));
        }
    }


    private void validateUserBalance(User user, BigDecimal totalPrice) {
        if (user.getBalance().compareTo(totalPrice) < 0 ) {
            logEventService.save(LogEvent.builder()
                            .text("У пользователя " + user.getName() + " недостаточно средств для покупки")
                            .method(this.getClass().getSimpleName() + ".cartPurchases(Long userId, List<CartItem> cartItems).validateUserBalance(User user, BigDecimal totalPrice)")
                            .createAt(OffsetDateTime.now())
                            .type(Type.ERROR)
                    .build());
            throw new UserHasNotEnoughMoneyOnBalanceException();
        }
    }

}
