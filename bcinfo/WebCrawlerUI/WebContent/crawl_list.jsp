<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.CrawlList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>抓取列表</title>
</head>
<body>
<a href="./crawl_list_add.jsp">添加</a>
<%
	List<CrawlList> list = (List<CrawlList>)request.getAttribute("crawlList");
%>
<table>
	<tr>
		<td>CRAWL_ID</td>
		<td>CHANNEL_ID</td>
		<td>CRAWL_URL</td>
		<td>CRAWL_STATUS</td>
		<td>CREATE_TIME</td>
	</tr>
	<%
		if(list != null && !list.isEmpty()){
			for(CrawlList crawl : list){
	%>
	<tr>
		<td><%=crawl.getCrawlId() %></td>
		<td><%=crawl.getChannelId() %></td>
		<td><%=crawl.getCrawlUrl() %></td>
		<td><%=crawl.getCrawlStatus() %></td>
		<td><%=crawl.getCreateTime() %></td>
	</tr>
	<%		
			}
		}
	%>
</table>
</body>
</html>