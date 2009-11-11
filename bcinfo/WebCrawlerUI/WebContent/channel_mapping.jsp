<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page
	import="com.bcinfo.wapportal.repository.crawl.domain.ChannelMapping"%>
<%@page
	import="com.bcinfo.wapportal.repository.crawl.dao.ChannelMappingDao"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>频道映射</title>

<!-- table -->
<link href="css/tablecloth/tablecloth.css" rel="stylesheet"
	type="text/css" media="screen" />
<script type="text/javascript" src="css/tablecloth/tablecloth.js"></script>
<!-- jQuery -->
<script type="text/javascript" src="js/jquery/jquery-1.3.2.js"></script>
<!-- 自定义脚本程序 -->
<script type="text/javascript">
	$(document).ready(function() {
		$("#delBtn").bind('click', function() {
			$("#type").val("del");
		});
	});
</script>
</head>
<body>

<%
	Long userId = (Long)session.getAttribute("userId");
	
	ChannelMappingDao dao = new ChannelMappingDao();
	List<ChannelMapping> list = null;
	if(userId != null)
		list = dao.getChannelMappingList(userId,null);
	else
		list = new ArrayList();
	List<Channel> channelList = new ChannelDao().getChannels(null);
	int count = (list != null) ? list.size() : 0;
%>

<form action="./ChannelMappingServlet" method="post">
<input type="hidden" id="type" name="type" value="add">
<table align="center">
	<tr>
		<td>地方栏目ID: <input type="text" value="" name="localChannelId" />
		</td>
		<td>中心频道： <select name="channelId">
			<c:forEach items="<%=channelList %>" var="chl">
				<option value="${chl.channelId }">${chl.channelName }</option>
			</c:forEach>
		</select></td>
		<td align="center"><input type="submit" value="订购" /><input
			type="submit" value="退订" id="delBtn" /></td>
	</tr>
</table>
<table>
	<tr>
		<td>序号</td>
		<td>门户</td>
		<td>地方</td>
		<td>中心</td>
	</tr>
	<c:if test="<%=(count >0) %>">
		<c:forEach items="<%=list %>" var="obj">
			<tr>
				<td>${obj.mappingId }<input type="checkbox" name="mappingId"
					value="${obj.mappingId }"></td>
				<td>${obj.localCode }</td>
				<td>${obj.localChannelId }</td>
				<td>${obj.channelId }</td>
			</tr>
		</c:forEach>
	</c:if>
</table>
</form>
</body>
</html>