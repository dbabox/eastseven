<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>菜单</title>
</head>
<body>
<%Long userId = (Long)session.getAttribute("userId"); %>
<a href="channel_management.jsp" target="mainFrame">频道管理</a>
<a href="crawl_list.jsp" target="mainFrame">抓取配置</a>
<a href="channel_mapping.jsp" target="mainFrame">订购管理</a>
<a href="WebCrawlerUI.doc" target="_blank">使用手册下载</a>
<a href="login.jsp" target="_blank">退出</a>
</body>
</html>