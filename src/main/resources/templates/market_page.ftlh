<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Маркет — скины CS2</title>
    <link rel="stylesheet" href="/css/market.css">
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
<#include "header.ftlh">
<div class="market-container">
    <div class="filters-bar">
        <div class="search-wrapper">
            <input type="text" id="searchInput" placeholder="Поиск по названию..." class="search-input">
            <button class="search-btn">🔍</button>
        </div>

        <div class="filter-group">
            <label>Категория
                <select id="weaponCategory">
                    <option value="">Все</option>
                    <#list weaponCategories as category>
                        <option value="${category}">${category.categoryName}</option>
                    </#list>
                </select>
            </label>

            <label>Тип оружия
                <select id="weaponType">
                    <option value="">Все</option>
                    <#list weaponTypes as type>
                        <option value="${type}">${type.weaponName}</option>
                    </#list>
                </select>
            </label>

            <label>Редкость
                <select id="rarity">
                    <option value="">Все</option>
                    <#list rarities as rarity>
                        <option value="${rarity}">${rarity.rareName}</option>
                    </#list>
                </select>
            </label>

            <label>Износ
                <select id="wear">
                    <option value="">Все</option>
                    <#list wears as wear>
                        <option value="${wear}">${wear.wearName}</option>
                    </#list>
                </select>
            </label>

            <input type="number" id="priceFrom" placeholder="Цена от" class="price-input">
            <input type="number" id="priceTo" placeholder="Цена до" class="price-input">

            <button type="button" id="filterBtn">Отфильтровать</button>
            <button type="button" id="resetFilterBtn">Сбросить</button>
        </div>
    </div>

    <div id="listingsContainer">
        <#include "listings-fragment.ftlh">
    </div>
</div>

<div id="notification" style="margin-top: 10px;"></div>

<script>

    $(document).ready(function() {
        function loadFilteredListings() {
            const data = {
                weaponCategory: $('#weaponCategory').val(),
                weaponType: $('#weaponType').val(),
                rarity: $('#rarity').val(),
                wear: $('#wear').val(),
                searchQuery: $('#searchInput').val(),
                priceFrom: $('#priceFrom').val(),
                priceTo: $('#priceTo').val()
            };

            const csrfToken = $('meta[name="_csrf"]').attr('content');
            const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

            $.ajax({
                url: '/market/filter',
                type: 'GET',
                data: data,
                headers: csrfToken && csrfHeader ? { [csrfHeader]: csrfToken } : {},
                success: function(html) {
                    $('#listingsContainer').html(html);
                    $('#notification').html('<span style="color:green">Фильтр применён</span>').fadeOut(3000);
                },
                error: function(xhr) {
                    let errorMsg = 'Ошибка фильтрации';
                    if (xhr.responseJSON && xhr.responseJSON.description) {
                        errorMsg = xhr.responseJSON.description;
                    } else if (xhr.responseJSON && xhr.responseJSON.exceptionMessage) {
                        errorMsg = xhr.responseJSON.exceptionMessage;
                    }
                    $('#notification').html('<span style="color:red">' + errorMsg + '</span>');
                }
            });
        }

        $('#filterBtn').on('click', loadFilteredListings);

        $('#resetFilterBtn').on('click', function() {
            $('#weaponCategory').val('');
            $('#weaponType').val('');
            $('#rarity').val('');
            $('#wear').val('');
            $('#priceFrom').val('');
            $('#priceTo').val('');
            loadFilteredListings();
        });

        $('.price-input, #searchInput').on('keypress', function(e) {
            if (e.which === 13) loadFilteredListings();
        });

        $('.search-btn').on('click', loadFilteredListings);
    });
</script>

</body>
