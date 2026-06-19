<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Продажа скина | ${skinTemplate.name}</title>
    <link rel="stylesheet" href="/css/sellingSkin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<#include "header.ftlh">

<main class="detail-container">
    <div class="main-offer-card">
        <div class="skin-header">
            <img src="${skinTemplate.imageUrl!''}" class="skin-image-large" alt="${skinTemplate.name}">
            <div class="skin-info">
                <h1 class="skin-name">${skinTemplate.name}</h1>
                <div class="specs">
                    <span>Категория: ${skinTemplate.weaponCategory.name()}</span>
                    <span>Тип: ${skinTemplate.weaponType.name()}</span>
                    <span>Редкость: ${skinTemplate.rarity.name()}</span>
                    <span>Поношенность: ${skinTemplate.wear.name()}</span>
                </div>
                <div class="price-block">
                    <#if listingWithSmallestPrice??>
                        <div class="price-block">
                            <span class="price-label">Минимальная цена:</span>
                            <span class="price-value">${listingWithSmallestPrice.price} ₽</span>
                        </div>
                    <#else>
                        <div class="price-block">
                            <span class="price-label">Нет активных предложений</span>
                        </div>
                    </#if>
                </div>
            </div>
        </div>

        <div class="action-buttons">
            <#if listingWithSmallestPrice?? && user?? && listingWithSmallestPrice.seller?? && user.id == listingWithSmallestPrice.seller.id>
                <span class="your-item-badge">Ваш товар</span>
            <#elseif listingWithSmallestPrice??>
                <form method="post"
                      action="/market/${skinTemplate.weaponCategory.name()}/${skinTemplate.weaponType.name()}/${skinTemplate.slug}/fastBuy"
                      style="display: inline-block;">
                    <input type="hidden" name="listingId" value="${listingWithSmallestPrice.listingId}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <button type="submit" class="btn-fast-buy">Быстрая покупка</button>
                </form>
                <form method="post"
                      action="/market/${skinTemplate.weaponCategory.name()}/${skinTemplate.weaponType.name()}/${skinTemplate.slug}/addInCart"
                      style="display: inline-block;">
                    <input type="hidden" name="listingId" value="${listingWithSmallestPrice.listingId}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <button type="submit" class="btn-cart">Добавить в корзину</button>
                </form>
            <#else>
                <span class="no-offers">Нет предложений</span>
            </#if>
        </div>
    </div>

    <div class="price-history-section">
        <h2>История продаж</h2>
        <div style="border:1px solid red; padding:5px; margin:5px;">
            Размер priceHistory: ${priceHistory?size}
            <br>
            Первая цена: ${priceHistory[0].price!}
        </div>
        <canvas id="priceHistoryChart"></canvas>
    </div>

    <div class="other-offers">
        <h2 class="offers-title">Другие предложения</h2>
        <#if orderedListings?has_content>
            <div class="offers-grid">
                <#list orderedListings as listing>
                    <#assign skin = listing.skin/>
                    <div class="offer-card">
                        <div class="offer-details">
                            <span class="wear">${skin.wear.name()}</span>
                            <span class="float">Float: ${skin.skinFloat}</span>
                            <span class="price">${listing.price} ₽</span>
                        </div>
                        <div class="offer-action">
                            <#if user?? && listing.seller?? && user.id == listing.seller.id>
                                <span class="your-item-badge small">Ваш товар</span>
                            <#else>
                                <form method="post"
                                      action="/market/${skinTemplate.weaponCategory.name()}/${skinTemplate.weaponType.name()}/${skinTemplate.slug}/fastBuy"
                                      style="display: inline-block;">
                                    <input type="hidden" name="listingId" value="${listing.listingId}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    <button type="submit" class="btn-fast-buy-small">Быстрая покупка</button>
                                </form>
                                <form method="post"
                                      action="/market/${skinTemplate.weaponCategory.name()}/${skinTemplate.weaponType.name()}/${skinTemplate.slug}/addInCart"
                                      style="display: inline-block;">
                                    <input type="hidden" name="listingId" value="${listing.listingId}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    <button type="submit" class="btn-add-cart-small">🛒</button>
                                </form>
                            </#if>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="empty-message">
                <p>😕 Товаров не найдено</p>
            </div>
        </#if>
    </div>
</main>

<script>
    const labels = [
        <#list priceHistory as point>
        "${point.soldAt!''}",
        </#list>
    ];
    const prices = [
        <#list priceHistory as point>
        ${point.price?c},
        </#list>
    ];
    const ctx = document.getElementById('priceHistoryChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Цена продажи',
                    data: prices,
                    borderColor: '#1a73e8',
                    backgroundColor: 'rgba(26,115,232,0.1)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'top' },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.raw + ' ₽';
                            }
                        }
                    }
                },
                scales: {
                    y: { ticks: { callback: function(value) { return value + ' ₽'; } } }
                }
            }
        });
    }
    document.getElementById('debugLabels').innerText = JSON.stringify(labels);
    document.getElementById('debugPrices').innerText = JSON.stringify(prices);
</script>
</body>
</html>