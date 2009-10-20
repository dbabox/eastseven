<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>添加</title>
</head>
<body>
<form action="./CrawlListServlet?method=add" method="post">

<table align="center">
	<tr>
		<td>频道ID</td>
		<td></td>
	</tr>
	<tr>
		<td>抓取地址</td>
		<td>
			<input type="text" name="url">
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" value="保存"><input type="reset" value="重置">
		</td>
	</tr>
</table>
</form>
</body>
</html>