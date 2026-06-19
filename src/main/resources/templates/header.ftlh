<link rel="stylesheet" href="/css/header.css">
<header class="site-header">
    <nav class="main-nav">
        <a href="/market" class="nav-link">Магазин</a>
        <a href="/profile" class="nav-link">Профиль</a>
        <a href="/cart" class="nav-link">Корзина</a>
        <#if user??>
            <a href="/balance" class="balance-link">${user.balance} ₽</a>
            <#if user?? && user.imageUrl?? && user.imageUrl?has_content>
                <img src="${user.imageUrl}" width="50" height="70" alt="${user.name}" class="avatar">
            <#else>
                <img src="/images/defaultAvatarImage.webp" width="50" height="70" alt="Аватар по умолчанию" class="avatar">
            </#if>
        </#if>
    </nav>
    <div class="user-info">
        <#if user??>
            <span class="greeting">Привет, ${user.name}!</span>
            <a href="/logout" class="logout-link">Выйти</a>
        <#else>
            <a href="/signIn" class="login-link">Войти</a>
        </#if>
    </div>
</header>