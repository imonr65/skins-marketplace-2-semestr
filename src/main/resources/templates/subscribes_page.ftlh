<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Подписки</title>
    <link rel="stylesheet" href="/css/subscribes.css">
</head>
<body>
<#include "header.ftlh">
<div class="container">
    <h1 style="text-align: center">Выберите подписку</h1>
    <div class="subscriptions-list">
        <#if subscribes?? && subscribes?size gt 0>
            <#list subscribes as sub>
                <div class="card" data-price-per-month="${sub.price}">
                    <h3>${sub.typeName}</h3>
                    <div class="price">
                        ${sub.price} ₽ <span>/ месяц</span>
                    </div>
                    <form action="/subscribes/subscribe" method="post">
                        <input type="text" name="typeName" value="${sub.typeName}">

                        <label for="duration-${sub.typeName}">Срок подписки:</label>
                        <select id="duration-${sub.typeName}" name="duration" class="duration-select">
                            <option value="1">1 месяц</option>
                            <option value="3">3 месяца</option>
                            <option value="6">6 месяцев</option>
                            <option value="12">12 месяцев</option>
                        </select>

                        <div class="total-price">
                            Итого: <span class="total-amount">${sub.price}</span> ₽
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit">Подписаться</button>
                    </form>
                </div>
            </#list>
        <#else>
            <p>Нет доступных подписок</p>
        </#if>
    </div>
    <div class="note">
        * Стоимость подписки списывается сразу за выбранный период.
    </div>
</div>

<script>
    document.querySelectorAll('.card').forEach(card => {
        const pricePerMonth = parseFloat(card.dataset.pricePerMonth);
        const select = card.querySelector('.duration-select');
        const totalSpan = card.querySelector('.total-amount');

        function updateTotal() {
            const months = parseInt(select.value);
            const total = pricePerMonth * months;
            totalSpan.textContent = total.toFixed(2);
        }

        select.addEventListener('change', updateTotal);
        updateTotal();
    });
</script>
</body>
</html>