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
<title>Ƶ��ӳ��</title>
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
		<td>�Ż��� <select name="localCode">
			<option value="028" selected="selected">�Ĵ�</option>
			<option value="0791">����</option>
		</select></td>
	</tr>
	<tr>
		<td>�ط���ĿID: <input type="text" value="" name="localChannelId" />
		</td>
	</tr>
	<tr>
		<td>����Ƶ����
			<select name="channelId">
				<c:forEach items="<%=channelList %>" var="chl">
				<option value="${chl.channelId }">${chl.channelName }</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="����" /></td>
	</tr>
</table>
<table>
	<tr>
		<td>���</td>
		<td>�Ż�</td>
		<td>�ط�</td>
		<td>����</td>
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