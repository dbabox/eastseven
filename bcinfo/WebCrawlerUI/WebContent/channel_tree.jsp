<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.bcinfo.wapportal.repository.crawl.dao.ChannelDao"%>
<%@page import="java.util.List"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.domain.Channel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Tree</title>
<!-- http://www.meizz.com/Web/Plugs/MzTreeView10.js -->
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
  /*
  tree.nodes["1_100"] = "text:����ʾ��; data:id=100"; 
  tree.nodes["1_200"] = "text:÷��ѩ�ű��ؼ���; data:id=200";
  tree.nodes["1_310"] = "text:CSS; icon:css; data:id=310"; 
  tree.nodes["1_320"] = "text:DHTML; data:id=320"; 
  tree.nodes["1_300"] = "text:HTML; data:id=300"; 
  tree.nodes["1_400"] = "text:JavaScript; icon:book; data:id=400";
  tree.nodes["320_322"] = "text:����; icon: property; data:id=322"; 
  tree.nodes["320_323"] = "text:����; data:id=323"; 
  tree.nodes["320_324"] = "text:�¼�; icon:event; data:id=324"; 
  tree.nodes["320_325"] = "text:����; data:id=325"; 
  tree.nodes["400_407"] = "text:����; data:id=407"; 
  tree.nodes["400_406"] = "text:����; data:id=406"; 
  tree.nodes["400_408"] = "text:�����; data:id=408"; 
  tree.nodes["400_409"] = "text:����; data:id=409"; 
  tree.nodes["407_1140"] = "text:Date; url:Article.asp; data:id=140";
  tree.nodes["406_1127"] = "text:toString; url:Article.asp; data:id=127";
  tree.nodes["408_1239"] = "text:||; url:Article.asp; data:id=239";
  tree.nodes["409_1163"] = "text:E;  url:Article.asp; data:id=163";
  */
  <%
  ChannelDao dao = new ChannelDao();
  List<Channel> list = dao.getChannels(null);
  String nodes = dao.getChannelTree(list);
  if(nodes != null){
	  out.print(nodes);
  }
  %>
  tree.setURL("Catalog.asp");
  tree.setTarget("MzMain");
  document.write(tree.toString());    //����� obj.innerHTML = tree.toString();
</script>

</head>
<body>



</body>
</html>