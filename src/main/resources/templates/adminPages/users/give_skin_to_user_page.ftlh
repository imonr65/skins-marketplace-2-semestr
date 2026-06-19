<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Give skin to user</title>
</head>
<body>
<form action="/admin/users/giveSkins" method="post">
    <label>Email: <input name="email" type="email" required></label><br>
    <label>Skin name: <input name="name" type="text" required></label><br>

    <label>Категория Оружия:
        <select name="weaponCategory">
            <#list weaponCategories as category>
                <option value="${category}">${category.categoryName}</option>
            </#list>
        </select>
    </label><br>

    <label>Тип оружия:
    <select name="weaponType" required>
        <#list weaponTypes as type>
            <option value="${type}">${type.weaponName}</option>
        </#list>
    </select>
    </label><br>

    <label>Тип редкости:
    <select name="rarity" required>
        <#list rarities as rariry>
            <option value="${rariry}">${rariry.rareName}</option>
        </#list>
    </select>
    </label><br>

    <label>Skin float:
        <input name="skinFloat" type="number" step="0.001" min="0.001" max="0.999">
    </label><br>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit">Создать пользователю скин</button>
</form>
</body>
</html>