<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!--引入jsencrypt.js-->
    <script src="https://cdn.bootcss.com/jsencrypt/3.0.0-beta.1/jsencrypt.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>InfoSec: 登录</title>
</head>
<body>
<h1>登陆</h1>
<br>
<form action="http://localhost:8080/infosec/login" method="post">
    <input type="text" required="required" placeholder="用户" name="username"></input>
    <input type="password" required="required" placeholder="密码" name="password"></input>
    <button class="but" type="submit">登录</button>
</form>
</body>
</html>