<#include "../header.ftlh">
<div class="error-container">
    <h1>405</h1>
    <h2>Метод не поддерживается</h2>
    <p>${message!('Используемый HTTP-метод не разрешён для этого URL')}</p>
    <a href="/" class="btn">На главную</a>
</div>