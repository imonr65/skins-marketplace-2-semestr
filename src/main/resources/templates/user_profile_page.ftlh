<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/profile.css">
    <title>Мой профиль</title>
</head>
<body>
<#include "header.ftlh">

<main class="profile-container">
    <h1 class="profile-title">Профиль пользователя</h1>

    <div class="profile-card">
        <div class="avatar-section">
            <#if user?? && user.imageUrl?? && user.imageUrl?has_content>
                <img src="${user.imageUrl}" class="profile-avatar" alt="${user.name}">
            <#else>
                <img src="/images/defaultAvatarImage.webp" class="profile-avatar" alt="Аватар по умолчанию">
            </#if>
        </div>
        <div class="info-section">
            <p><strong>Имя:</strong> ${user.name}</p>
            <p><strong>Email:</strong> ${user.email}</p>
        </div>
    </div>

    <div class="section-box">
        <h2>Пополнение баланса</h2>
        <a href="/balance" class="btn-secondary">Пополнить баланс</a>
    </div>

    <div class="section-box">
        <h3>Моя подписка</h3>
        <#if user.cashbackSubscribe?? && user.cashbackSubscribe.subscribeType != "WITHOUT">
            <p><strong>Тип:</strong> ${user.cashbackSubscribe.subscribeType}</p>
            <p><strong>Действует до:</strong> ${user.cashbackSubscribe.subscriptionEndTime}</p>
            <p><strong>Кэшбэк:</strong>
                <#switch user.cashbackSubscribe.subscribeType>
                    <#case "COMMON">3%<#break>
                    <#case "SILVER">5%<#break>
                    <#case "GOLD">10%<#break>
                    <#default>0%
                </#switch>
            </p>
            <a href="/subscribes" class="btn-subscribe">Изменить подписку</a>
        <#else>
            <p>У вас нет активной подписки</p>
            <a href="/subscribes" class="btn-subscribe">Выбрать подписку</a>
        </#if>
    </div>

    <div class="action-links">
        <a href="/profile/update-profile">✏️ Редактировать профиль</a>
        <a href="/listings/my">📦 Мои продаваемые скины</a>
    </div>

    <div class="section-box inventory-box">
        <h2>Инвентарь</h2>
        <div class="inventory-grid">
            <#if userInventory?? && userInventory.inventorySkins?size gt 0>
                <#list inventory as item>
                    <div class="skin-card">
                        <#if item.template.imageUrl??>
                            <img src="${item.template.imageUrl}" class="skin-image" alt="${item.template.name}">
                        </#if>
                        <div class="skin-info">
                            <strong>${item.template.name}</strong>
                            <span>Оружие: ${item.template.weaponType.name()}</span>
                            <span>Редкость: ${item.template.rarity.name()}</span>
                            <span>Float: ${item.skinFloat?string("0.000")}</span>
                        </div>
                        <#if activeListings[item.id?string]??>
                            <p class="listing-status">✅ Уже в продаже по цене ${activeListings[item.id?string].price} руб.</p>
                        <#else>
                            <form action="/profile/skin/${item.id}/list" method="post" class="sell-form">
                                <input type="number" name="price" step="0.01" min="0.01" placeholder="Цена" required>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                <button type="submit" class="btn-sell">Продать</button>
                            </form>
                        </#if>
                    </div>
                </#list>
            <#else>
                <p class="empty-message">Инвентарь пуст</p>
            </#if>
        </div>
    </div>

    <div class="bottom-actions">
        <a href="/" class="btn-link">← На главную</a>
        <form action="/logout" method="post" class="logout-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <button type="submit" class="btn-logout">Выйти из аккаунта</button>
        </form>
    </div>
</main>
</body>
</html>