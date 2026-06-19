<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Skin Template Service</title>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
<h1>Создание шаблона скина</h1>
<div id="notification"></div>

<form id="skinTemplateForm">
    <input id="name" type="text" placeholder="Название скина" required>

    <label>Категория Оружия
    <select id="weaponCategory">
        <#list weaponCategories as category>
            <option value="${category}">${category.categoryName}</option>
        </#list>
    </select>
    </label><br>

    <label>Тип оружия</label>
    <select id="weaponType" required>
        <#list weaponTypes as type>
            <option value="${type}">${type.weaponName}</option>
        </#list>
    </select>

    <label>Тип редкости</label>
    <select id="rarity" required>
        <#list rarities as rariry>
            <option value="${rariry}">${rariry.rareName}</option>
        </#list>
    </select>

    <label>Степень поношенности</label>
    <select id="wear" required>
        <#list wears as wear>
            <option value="${wear}">${wear.wearName}</option>
        </#list>
    </select>
    <input id="imageUrl" type="text" placeholder="чисто пусто пока">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="button" onclick="saveSkinTemplate(
         $('#name').val(),
         $('#weaponCategory').val(),
         $('#weaponType').val(),
         $('#rarity').val(),
         $('#wear').val(),
         $('#imageUrl').val()
    )">Сохранить</button>

</form>
<script>
    function saveSkinTemplate(name, weaponCategory, weaponType, rarity, wear, imageUrl) {
        let data = {
            "name": name,
            "weaponCategory": weaponCategory,
            "weaponType": weaponType,
            "rarity": rarity,
            "wear": wear,
            "imageUrl": imageUrl
        }

        let csrfToken = $('input[name="_csrf"]').val();

        $.ajax({
            type: 'POST',
            url: '/admin/skin-templates/create',
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': csrfToken},
            success: function (response) {
                $('#notification').html('<span style="color:green">Шаблон "' + response.name + '" успешно создан</span>');
                $('#skinTemplateForm')[0].reset();
            },
            error: function (xhr) {
                let errorMsg = 'Ошибка';
                if (xhr.responseJSON && xhr.responseJSON.description) {
                    errorMsg = xhr.responseJSON.description;
                } else if (xhr.responseJSON && xhr.responseJSON.exceptionMessage) {
                    errorMsg = xhr.responseJSON.exceptionMessage;
                }
                $('#notification').html('<span style="color:red">' + errorMsg + '</span>');
            },
            dataType: "json",
            contentType: "application/json"
        })
    }

</script>
</body>
</html>