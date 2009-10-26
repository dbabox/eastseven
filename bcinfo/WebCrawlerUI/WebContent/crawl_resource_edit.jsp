<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.CrawlResource"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>编辑</title>

<!-- FCK -->
<script type="text/javascript" src="js/fckeditor/fckeditor.js"></script>

<script type="text/javascript">
	window.onload = function() {
		var oFCKeditor = new FCKeditor('FCKeditor1');
		oFCKeditor.BasePath = "js/fckeditor/";
		oFCKeditor.ToolbarSets = "Basic";
		oFCKeditor.ReplaceTextarea();
	}
</script>
</head>
<body>
<%
	CrawlResource resource = null;
	resource = (CrawlResource)request.getAttribute("resource");
%>
<form action="" method="post">

    <textarea id="FCKeditor1" rows="" cols="">
	<c:if test="<%=resource!=null %>">
		${resource.content }
	</c:if>
	</textarea>
</form>
</body>
</html>