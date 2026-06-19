<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/css/signUp.css">
    <title>Регистрация</title>
</head>
<body>
<div class="container">
    <h1>Регистрация</h1>
    <form action="/signUp" method="post">
        <label>Nickname:
            <input name="nickname" type="text" required>
        </label>
        <label>Email:
            <input name="email" type="email" required>
        </label>
        <label>Password:
            <input name="password" type="password" required>
        </label>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <button type="submit">Зарегистрироваться</button>
    </form>
    <div class="login-link">
        Уже есть аккаунт? <a href="/signIn">Авторизация</a>
    </div>
</div>
</body>
</html>