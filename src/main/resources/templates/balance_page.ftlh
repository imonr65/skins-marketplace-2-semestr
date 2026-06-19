<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Пополнение счета</title>
    <link rel="stylesheet" href="/css/balance.css">
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
<#include "header.ftlh">
<div class="container">
    <h1>Пополнение счета</h1>

    <div class="balance">
        Текущий баланс: <span>${user.balance} ₽</span>
    </div>

    <form action="/balance" method="post" id="balanceForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="form-group">
            <label for="sumToTopUpUserBalance">Сумма пополнения (₽):</label>
            <input type="number"
                   id="sumToTopUpUserBalance"
                   name="sumToTopUpUserBalance"
                   step="1"
                   min="1"
                   required
                   placeholder="Введите сумму">
            <div class="error" id="sumError"></div>
        </div>

        <div class="preset-buttons">
            <button type="button" class="preset-btn" data-amount="100">+100 ₽</button>
            <button type="button" class="preset-btn" data-amount="500">+500 ₽</button>
            <button type="button" class="preset-btn" data-amount="1000">+1000 ₽</button>
            <button type="button" class="preset-btn" data-amount="2500">+2500 ₽</button>
            <button type="button" class="preset-btn" data-amount="5000">+5000 ₽</button>
        </div>

        <button type="submit">Пополнить</button>
    </form>
    <div class="note">
        * Минимальная сумма пополнения — 1 ₽.
    </div>
</div>

<script>
    const presetButtons = document.querySelectorAll('.preset-btn');
    const sumInput = document.getElementById('sumToTopUpUserBalance');
    const errorDiv = document.getElementById('sumError');

    presetButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const amount = btn.getAttribute('data-amount');
            sumInput.value = amount;
            errorDiv.textContent = '';
        });
    });

    const form = document.getElementById('balanceForm');
    form.addEventListener('submit', (e) => {
        const value = parseFloat(sumInput.value);
        if (isNaN(value) || value < 1) {
            e.preventDefault();
            errorDiv.textContent = 'Сумма должна быть не менее 1 ₽';
        } else if (!Number.isInteger(value)) {
            e.preventDefault();
            errorDiv.textContent = 'Сумма должна быть целым числом (копейки не поддерживаются)';
        } else {
            errorDiv.textContent = '';
        }
    });
</script>
</body>
</html>