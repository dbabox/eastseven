<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>登录</title>
</head>
<body>
<form action="./LoginServlet" method="post">
<table align="center">
	<tr>
		<td>帐号</td>
		<td><input type="text" name="userName" value=""></td>
	</tr>
	<tr>
		<td>密码</td>
		<td><input type="password" name="password" value=""></td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" value="登录"> <input
			type="reset" value="重置"></td>
	</tr>
</table>
</form>
</body>
</html>