<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Корзина</title>
    <link rel="stylesheet" href="/css/cart.css">
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
<#include "header.ftlh">
<div class="cart-container">
    <h1>Корзина</h1>

    <#if cartItems?? && cartItems?has_content>
        <form id="cartForm">
            <table class="cart-table">
                <thead>
                <tr>
                    <th>Выбрать</th>
                    <th>Скин</th>
                    <th>Float</th>
                    <th>Цена</th>
                </tr>
                </thead>
                <tbody>
                <#list cartItems as cartItem>
                    <tr data-price="${cartItem.listing.price?string.computer}">
                        <td>
                            <input type="checkbox" class="item-checkbox" name="cartItemId" value="${cartItem.id}" data-price="${cartItem.listing.price}" checked>
                        </td>
                        <td>
                            <#if cartItem.listing.skin.template.imageUrl?? && cartItem.listing.skin.template.imageUrl?has_content>
                                <img src="${cartItem.listing.skin.template.imageUrl}" width="80" height="120">
                            </#if>
                            ${cartItem.listing.skin.template.name}
                        </td>
                        <td>${cartItem.listing.skin.skinFloat}</td>
                        <td class="item-price">${cartItem.listing.price} ₽</td>
                    </tr>
                </#list>
                </tbody>
            </table>

            <div class="cart-summary">
                <p>Общая сумма: <span id="totalPrice">0</span> ₽</p>
                <button type="button" id="buySelectedBtn" class="btn-primary">Купить выбранное</button>
            </div>
        </form>
    <#else>
        <p>Корзина пуста</p>
    </#if>
</div>

<script>
    $(document).ready(function() {
        // Функция пересчёта общей суммы
        function updateTotalPrice() {
            let total = 0;
            $('.item-checkbox:checked').each(function() {
                total += parseFloat($(this).data('price'));
            });
            $('#totalPrice').text(total.toFixed(2));
        }

        // При изменении состояния чекбокса пересчитываем
        $(document).on('change', '.item-checkbox', function() {
            updateTotalPrice();
        });

        // При загрузке страницы считаем сумму (все чекбоксы по умолчанию отмечены)
        updateTotalPrice();

        // Обработчик кнопки "Купить выбранное"
        $('#buySelectedBtn').on('click', function() {
            let selectedIds = [];
            $('.item-checkbox:checked').each(function() {
                selectedIds.push($(this).val());
            });

            if (selectedIds.length === 0) {
                alert('Выберите хотя бы один товар');
                return;
            }

            $.ajax({
                url: '/cart/buy',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ cartItemIds: selectedIds }),
                headers: { 'X-Requested-With': 'XMLHttpRequest' },
                success: function(response) {
                    alert('Покупка успешно выполнена!');
                    location.reload();
                },
                error: function(xhr) {
                    let errorMsg = 'Ошибка при покупке';
                    if (xhr.responseJSON && xhr.responseJSON.description) {
                        errorMsg = xhr.responseJSON.description;
                    } else if (xhr.responseJSON && xhr.responseJSON.exceptionMessage) {
                        errorMsg = xhr.responseJSON.exceptionMessage;
                    }
                    alert(errorMsg);
                }
            });
        });
    });
</script>
</body>
</html>