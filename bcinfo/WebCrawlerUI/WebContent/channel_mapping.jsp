<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.ChannelMapping"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelMappingDao"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>频道映射</title>
</head>
<body>

<%
	List<ChannelMapping> list = new ChannelMappingDao().getAllChannelMappingList();
	List<Channel> channelList = new ChannelDao().getChannels(null);
	int count = (list!=null)?list.size():0;
	
%>

<form action="./ChannelMappingServlet" method="post">
<table align="center">
	<tr>
		<td>门户： <select name="localCode">
			<option value="028" selected="selected">四川</option>
			<option value="0791">江西</option>
		</select></td>
	</tr>
	<tr>
		<td>地方栏目ID: <input type="text" value="" name="localChannelId" />
		</td>
	</tr>
	<tr>
		<td>中心频道：
			<select name="channelId">
				<c:forEach items="<%=channelList %>" var="chl">
				<option value="${chl.channelId }">${chl.channelName }</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="订购" /></td>
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
		<td>${obj.mappingId }</td>
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