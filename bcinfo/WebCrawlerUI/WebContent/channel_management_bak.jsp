<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ƶ������</title>

<link href="css/tablecloth/tablecloth.css" rel="stylesheet"
	type="text/css" media="screen" />
<!-- table 
<script type="text/javascript" src="css/tablecloth/tablecloth.js"></script>
-->
<script type="text/javascript">
$(document).ready(function() {
	$('tr:odd').addClass('odd');
	$('tr:even').addClass('even');
}
</script>
</head>
<body>
<%
	ChannelDao dao = new ChannelDao();
	List<Channel> list = null;
	list = dao.getChannels(null);
	List<Channel> select = null;
	select = dao.getChannels();
	
%>

<form action="./ChannelManagementServlet?method=add" method="post">
<table>
	<tr>
		<td>����Ƶ�� <select name="channelPid">
			<c:if test="<%= list!=null %>">
				<c:forEach items="<%=list %>" var="obj" >
					<option value="${obj.channelId }" style="width: 200px">
					${obj.channelName }
					</option>
				</c:forEach>
			</c:if>
		</select></td>
		<td>Ƶ������<input type="text" name="channelName" value=""></td>
		<td><input type="submit" value="���"></td>
	</tr>
</table>
</form>

<table>
	<tr>
		<td>Ƶ�����</td>
		<td>����Ƶ�����</td>
		<td>Ƶ������</td>
		<td>����ʱ��</td>
	</tr>
	<c:if test="<%= list!=null %>">
		<c:forEach items="<%=list %>" var="obj">

			<tr>
				<td>${obj.channelId }</td>
				<td>${obj.channelPid }</td>
				<td>${obj.channelName }</td>
				<td>${obj.createTime }</td>
			</tr>

		</c:forEach>
	</c:if>
</table>
</body>
</html>