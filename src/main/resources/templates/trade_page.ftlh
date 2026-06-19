<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Страница Обмена</title>
</head>
<body>

<form action="/trade" method="post" id="tradeForm">
    <#list userInventorySkins as skin>
        <div>
            <input type="checkbox" name="skinsId" value="${skin.id}" data-rarity="${skin.template.rarity}">
            <img src="${skin.template.imageUrl}" width="50">
            ${skin.template.name} (${skin.template.rarity.rareName})
        </div>
    </#list>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit">Обменять</button>
</form>

<script>
    const form = document.getElementById('tradeForm');
    form.addEventListener('submit', (e) => {
        const checked = document.querySelectorAll('input[name="skinsId"]:checked');
        if (checked.length < 5 || checked.length > 10) {
            e.preventDefault();
            alert('Выберите от 5 до 10 скинов');
            return;
        }
        const rarities = [...checked].map(cb => cb.dataset.rarity);
        const first = rarities[0];
        if (!rarities.every(r => r === first)) {
            e.preventDefault();
            alert('Все выбранные скины должны быть одной редкости');
        }
    });
</script>
</body>
</html>