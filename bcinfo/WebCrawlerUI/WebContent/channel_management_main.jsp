<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>频道管理列表</title>

<link href="css/tablecloth/tablecloth.css" rel="stylesheet"
	type="text/css" media="screen" />

</head>
<body>
<%
	List<Channel> list = (List<Channel>) request.getAttribute("channelList");
	String channelId = (String)request.getAttribute("channelId");
%>
<form action="./ChannelManagementServlet?method=add&channelId=<%=channelId %>" method="post">
<table align="center">
	<tr>
		<td>频道名称<input type="text" id="channelName" name="channelName" value=""><input type="submit" value="添加"></td>
	</tr>
</table>
</form>
<table align="center">
	<tr>
		<td align="center"><strong>序号</strong></td>
		<td align="center"><strong>操作</strong><input type="checkbox"
			id="checkBox" /></td>
		<td align="center"><strong>名称</strong></td>
	</tr>
	<c:if test="<%=list!=null %>">
		<c:forEach items="<%=list %>" var="channel" varStatus="status">
			<tr>
				<td align="center">${status.index+1 }</td>
				<td align="center"><input type="checkbox"
					id="id_${channel.channelId }" name="checkStatus"
					value="${channel.channelId }" /></td>
				<td align="center">${channel.channelName }</td>
			</tr>
		</c:forEach>
	</c:if>
</table>
</body>
</html>