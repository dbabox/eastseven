<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ƶ���������β˵�</title>

<!-- DTree -->
<link rel="StyleSheet" href="js/dtree/dtree.css" type="text/css" />
<script type="text/javascript" src="js/dtree/dtree.js"></script>

</head>
<body>
<div class="dtree"><a href="javascript: d.openAll();">ȫ��չ��</a> | <a
	href="javascript: d.closeAll();">ȫ���۵�</a> <script
	type="text/javascript">
	<%ChannelDao dao = new ChannelDao();

			out.print(dao.getChannelTreeForDTree(dao.getChannels(null),"ChannelManagementServlet","channelMainFrame"));%>
	</script></div>
</body>
</html>