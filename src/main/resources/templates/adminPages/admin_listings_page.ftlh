<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Listings</title>
</head>
<body>

<h2>Создать запись о продаже</h2>
<form action="/admin/listings/create-listing" method="post">
    <label>Почта пользователя:
        <input name="userEmail" type="text" required>
    </label><br>
    <label>Флоат скина:
        <input name="skinFloat" type="number" step="0.001" min="0.001" max="0.999" required>
    </label><br>
    <label>Название скина
        <input name="skinTemplateName" type="text" required>
    </label><br>
    <label>Цена:
        <input name="price" type="number" step="0.1" min="0.1" required>
    </label><br>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit">Создать</button>
</form>

<h2>Все списки записей о продаже</h2>
<#if listings?has_content>
   <#list listings as listing>
       <li>
           <strong>Название скина:</strong> ${listing.skin.template.name}<br/>
           <strong>Статус:</strong> ${listing.listingStatus.name()}<br/>
           <strong>Цена:</strong> ${listing.price}<br/>
       </li>
   </#list>
<#else>
    <p>Нет доступных объявлений.</p>
</#if>
</body>
</html>