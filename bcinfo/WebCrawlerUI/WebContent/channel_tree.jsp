<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%>
<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Tree</title>
<!-- 梅花雪 -->
<!--  
<script language="JavaScript" src="js/MzTreeView10/MzTreeView10.js"></script>
<style>
A.MzTreeview
{
  font-size: 9pt;
  padding-left: 3px;
}
</style>
-->
<!-- DTree -->
<link rel="StyleSheet" href="js/dtree/dtree.css" type="text/css" />
<script type="text/javascript" src="js/dtree/dtree.js"></script>

<script type="text/javascript">

<%
ChannelDao dao = new ChannelDao();

out.print(dao.getChannelTreeForDTree(dao.getChannels(null)));
%>

</script>

</head>
<body>



</body>
</html>