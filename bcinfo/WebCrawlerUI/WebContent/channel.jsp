<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>频道</title>
</head>
<frameset rows="40,*" cols="*" frameborder="no" border="0" framespacing="0">
	<frame src="channel_menu.jsp" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" />
	<frameset cols="180,*" frameborder="no" border="0" framespacing="0">
		<frame src="channel_tree.jsp" name="leftFrame" scrolling="auto" id="leftFrame" title="leftFrame" />
		<frame src="blank.html" name="mainFrame" id="mainFrame" title="mainFrame" scrolling="auto" />
	</frameset>
</frameset>
<body>
<%
%>
</body>
</html>