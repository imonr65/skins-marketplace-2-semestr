<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Избранное</title>
</head>
<body>
<h1>❤️ Избранные скины</h1>
<#if favorites?has_content>
    <div class="favorites-grid">
        <#list favorites as skinTemplate>
            <div class="favorite-card">
                <img src="${skinTemplate.imageUrl!''}" alt="нет изображения" width="200">
                <h3>${skinTemplate.name}</h3>
                <p>${skinTemplate.weaponType.name()}</p>
                <form method="post" action="/likes/remove/${skinTemplate.id}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit">Удалить</button>
                </form>
            </div>
        </#list>
    </div>
<#else>
    <p>У вас пока нет избранных скинов</p>
</#if>
</body>
</html>