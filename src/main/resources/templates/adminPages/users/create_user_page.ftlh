<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Create User</title>
</head>
<body>
<form action="/admin/users/createUser" method="post">
    <label>Nickname: <input name="nickname" type="text" required></label><br>
    <label>Email: <input name="email" type="email" required></label><br>
    <label>Password: <input name="password" type="password" required></label><br>
    <label>Role:
        <select name="role" required>
            <option value="ADMIN">ADMIN</option>
            <option value="USER">USER</option>
        </select>
    </label><br>
    <label>Balance: <input name="balance" type="number" min="0" value="0.00" required></label><br>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit">Create User</button>
</form>
</body>
</html>