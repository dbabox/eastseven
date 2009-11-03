<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.CrawlList"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.CrawlListDao"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ץȡ�б�</title>

<!-- table -->
<link href="css/tablecloth/tablecloth.css" rel="stylesheet"
	type="text/css" media="screen" />
<script type="text/javascript" src="css/tablecloth/tablecloth.js"></script>
<!-- jQuery -->
<script type="text/javascript" src="js/jquery/jquery-1.3.2.js"></script>
<!-- �Զ���ű����� -->
<script type="text/javascript">
	$(document).ready(function() {
		$("#startBtn").bind('click', function() {
			$("#type").val("start");
		});
		$("#stopBtn").bind('click', function() {
			$("#type").val("stop");
		});
		$("#addBtn").bind('click', function() {
			$("#type").val("add");
		});
	});
</script>
</head>
<body>
<%
	CrawlListDao dao = new CrawlListDao();
	List<CrawlList> list = dao.getAllCrawlList();
	List<Channel> channelList = new ChannelDao().getChannels(null);
%>
<form action="./CrawlListConfigServlet" method="post">
<table>
	<tr>
		<td><select name="channelId">
			<c:forEach items="<%=channelList %>" var="chl">
				<option value="${chl.channelId }">[${chl.channelId }]${chl.channelName }</option>
			</c:forEach>
		</select>
		<input type="text" name="crawlUrl" value="" alt="ץȡ��ַ" title="ץȡ��ַ">
		<input type="submit" value="�������" id="addBtn">
		<input type="submit" value="����" id="startBtn">
		<input type="submit" value="ͣ��" id="stopBtn">
		<input type="hidden" value="add" id="type" name="type">
		</td>
	</tr>
	<tr></tr>
</table>

<table>
	<tr>
		<td>���</td>
		<td>Ƶ��</td>
		<td>ץȡ��ַ</td>
		<td>ʹ��״̬</td>
		<td>����ʱ��</td>
	</tr>
	<c:if test="<%= list!=null %>">
		<c:forEach items="<%=list %>" var="obj" varStatus="status">
			<tr>
				<td>${status.index+1 }<input type="checkbox" name="crawlId"
					value="${obj.crawlId }"></td>
				<td>${obj.channelName }</td>
				<td>${obj.crawlUrl }</td>
				<td>${obj.crawlStatus }</td>
				<td>${obj.createTime }</td>
			</tr>
		</c:forEach>
	</c:if>
</table>
</form>
</body>
</html>