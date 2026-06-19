<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Document</title>
</head>
<body>
<#if listings?has_content>
    <div class="listings-grid">
        <#list listings as listing>
            <div class="listing-card">
                <div class="card-image">
                    <#if listing.skin.template.imageUrl?? && listing.skin.template.imageUrl?has_content>
                        <img src="${listing.skin.template.imageUrl}" alt="${listing.skin.template.name}" width="200" height="300">
                    <#else>
                        <div class="no-image">Нет изображения</div>
                    </#if>
                </div>
                <div class="card-content">
                    <h3 class="skin-name">${listing.skin.template.name}</h3>
                    <p class="wear">📉 Поношенность: ${listing.skin.template.wear.name()}</p>
                    <p class="price">💰 Цена: ${listing.price} ₽</p>
                    <div class="card-actions">
                        <form method="post" action="/likes/add/${listing.skin.template.id}">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="favorite-btn">
                                ❤️ В избранное
                            </button>
                        </form>
                        <a href="/market/${listing.skin.template.weaponCategory.categoryName}/${listing.skin.template.weaponType.weaponName}/${listing.skin.template.slug}" class="details-link">Подробнее</a>
                        <button class="cart-btn" onclick="alert('Товар добавлен в корзину ')">🛒 В корзину</button>
                    </div>
                </div>
            </div>
        </#list>
    </div>
<#else>
    <div class="empty-message">
        <p>😕 Товаров не найдено</p>
    </div>
</#if>
</body>
</html>