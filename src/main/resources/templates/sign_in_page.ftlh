<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Авторизация</title>
    <link rel="stylesheet" href="/css/signIn.css">
<body>
<div class="container">
    <h1>Авторизация</h1>
    <p>Для взаимодействия с сайтом авторизуйтесь</p>

    <form action="/signIn" method="post">
        <label>Email:
            <input name="email" type="email" required>
        </label>
        <label>Пароль:
            <input name="password" type="password" required>
        </label>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <button type="submit">Войти</button>
    </form>

    <div class="register-link">
        Нет аккаунта? <a href="/signUp">Зарегистрироваться</a>
    </div>
</div>
</body>
</html>