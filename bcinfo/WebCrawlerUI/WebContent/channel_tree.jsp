<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%>
<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Tree</title>
<script language="JavaScript" src="js/MzTreeView10/MzTreeView10.js"></script>

<style>
A.MzTreeview
{
  font-size: 9pt;
  padding-left: 3px;
}
</style>
<script language="JavaScript">
  var tree = new MzTreeView("tree");

  tree.icons["property"] = "property.gif";
  tree.icons["css"] = "collection.gif";
  tree.icons["book"]  = "book.gif";
  tree.iconsExpand["book"] = "bookopen.gif"; //չ��ʱ��Ӧ��ͼƬ

  tree.setIconPath("js/MzTreeView10/"); //�������·��

  tree.nodes["0_1"] = "text:Ƶ��";
  
  <%
  ChannelDao dao = new ChannelDao();
  //List<Channel> list = dao.getChannels(null);
  String nodes = dao.getChannelTree(/*list*/);
  if(nodes != null){
	  out.print(nodes);
  }
  %>
  tree.setURL("channel_tree.jsp");
  //tree.setTarget("MzMain");
  document.write(tree.toString());    //����� obj.innerHTML = tree.toString();
</script>

</head>
<body>



</body>
</html>