<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My listings</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<#include "header.ftlh">

<h2>Мои активные продажи</h2>
<div id="listingsContainer">
    <#if userListings?size gt 0>
        <#list userListings as listing>
            <div class="listing-item" data-listing-id="${listing.listingId}">
                <img src="${listing.skin.template.imageUrl!''}" width="100">
                <strong>${listing.skin.template.name}</strong><br>
                <span class="price-display">${listing.price} руб.</span><br>
                Просмотров: ${listing.viewsCount}<br>
                <form method="post" action="/listings/${listing.listingId}/update-price" style="display: inline-block;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="number" step="0.1" min="0.1" name="price" placeholder="Новая цена" required>
                    <button type="submit">Изменить цену</button>
                </form>
                <form method="post" action="/listings/${listing.listingId}/delete-listing" style="display: inline-block;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit">Снять с продажи</button>
                </form>
            </div>
        </#list>
    <#else>
        <p id="noListingsMsg">У вас нет активных объявлений.</p>
    </#if>
</div>

<a href="/profile">Назад в профиль</a> | <a href="/">На главную</a>
</body>
</html>