<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Редактирование профиля</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
<#include "header.ftlh">

<form action="/profile/update-profile" method="post">
    <div class="avatar-edit-container">
        <img id="avatarImage"
             src="${user.imageUrl! '/images/defaultAvatarImage.webp'}"
             alt="Аватар" width="100" height="100"
             style="cursor: pointer; border-radius: 50%; object-fit: cover;">
        <input type="file" id="avatarInput" accept="image/jpeg,image/png,image/webp" style="display: none;">
    </div>
    <input type="text" name="name" value="${user.name}">
    <input type="email" name="email" value="${user.email}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit">Сохранить</button>
    <a href="/profile">Отмена</a>
</form>

<div id="notification" style="position: fixed; top: 10px; right: 10px;"></div>

<script>
    function showNotification(message, type) {
        const $notify = $('#notification');
        $notify.html('<span style="color:' + (type === 'success' ? 'green' : 'red') + '">' + message + '</span>');
        setTimeout(() => $notify.empty(), 3000);
    }

    $(document).ready(function () {
        const $avatarImg = $('#avatarImage');
        const $fileInput = $('#avatarInput');

        $avatarImg.on('click', function () {
            $fileInput.click();
        });

        $fileInput.on('change', function () {
            const file = this.files[0];
            if (!file) return;

            const formData = new FormData();
            formData.append('avatar', file);

            const csrfToken = $('meta[name="_csrf"]').attr('content');
            const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
            const headers = {};
            if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

            $.ajax({
                url: '/profile/update-avatar',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                headers: headers,
                success: function (response) {
                    if (response.avatarUrl) {
                        $avatarImg.attr('src', response.avatarUrl);
                        // Если в шапке есть класс .header-avatar, обновите и его
                        $('.header-avatar').attr('src', response.avatarUrl);
                        showNotification('Аватар успешно обновлён', 'success');
                    }
                },
                error: function (xhr) {
                    let errorMsg = 'Ошибка загрузки аватара';
                    if (xhr.responseJSON && xhr.responseJSON.description) {
                        errorMsg = xhr.responseJSON.description;
                    }
                    showNotification(errorMsg, 'error');
                }
            });
        });
    });
</script>
</body>
</html>