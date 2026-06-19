<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Change User Role</title>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
<form action="/admin/users/changeRole" method="post">
    <label>Email:</label>
    <label>
        <input name="email" type="email" placeholder="EMAIL" required>
    </label>

    <label>Role:</label>
    <label>
        <select name="role" required>
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
        </select>
    </label>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="submit" value="Установить новую роль">
</form>

</body>
</html>