<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@page import="java.util.List"%>
<%@page
	import="com.bcinfo.wapportal.repository.crawl.domain.CrawlResource"%>
<%@page import="com.bcinfo.wapportal.repository.crawl.dao.CrawlResourceDao"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<!--  
<link type="text/css" href="http://jqueryui.com/latest/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="http://jqueryui.com/latest/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://jqueryui.com/latest/ui/ui.core.js"></script>
<script type="text/javascript" src="http://jqueryui.com/latest/ui/ui.draggable.js"></script>
<script type="text/javascript" src="http://jqueryui.com/latest/ui/ui.resizable.js"></script>
<script type="text/javascript" src="http://jqueryui.com/latest/ui/ui.dialog.js"></script>
-->
<link type="text/css" href="js/jquery/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.core.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.draggable.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.resizable.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.dialog.js"></script>
<!--  -->
<script type="text/javascript" src="js/json.js"></script>

<script type="text/javascript">
	/*
	var title = "";
	var cnt = "";
	var link = "";
	var status = "";
	*/
	var config = { 
			buttons: { "关闭": function() { $(this).dialog("close"); }
						//"审核通过": function(){},
						//"审核未通过": function(){},
						,"查看原网页": function(){
							alert($("#dialog_link").html());
							//window.location.replace($("#dialog_link").html());
						}//window.location.replace(link);
		 	},
			autoOpen: false,
			modal: true,
			width: 500,
			position: 'top'
	};
	
	$(document).ready(function() {
		//,height: 530,width: 500
		
		//初始化
		$("#dialog").dialog(config);

		//给所有tr td 绑定一个dialog
		$("tr td:first-child").bind('click',function(){
			//初始化dialog中的内容
			$("#dialog").empty();
			$("#dialog_link").empty();
			var res_id = $(this).html();
			
			//发送ajax请求
			$.get('./CrawlResourceServlet?method=detail',{resId:res_id},function(data){
				var json = eval('('+data+')');
				var title = json.title;
				var cnt = json.content;
				var link = json.link;//alert(link);
				var status = json.status;
				$("#dialog").append(cnt);
				$("#dialog_link").append(link);
				$("#dialog").dialog('option', 'title', title);
			});

			$("#dialog").dialog('open');
		});
	});
</script>
<title></title>
</head>
<body>
<div id="dialog"></div>
<div id="dialog_link"></div>
<%
	String pageSize = (request.getAttribute("pageSize")!=null)?(String)request.getAttribute("pageSize"):"15";
	String channelId = (String) request.getParameter("channelId");
	String title = (String) request.getParameter("title");
	if(title == null) title = "";
	if(channelId == null || "".equals(channelId)) channelId = (String)request.getAttribute("channelId");
%>
<%
	String method = (String)request.getParameter("method");
	List<CrawlResource> list = (List<CrawlResource>) request.getAttribute("crawlResourceList");
	int count = new CrawlResourceDao().getCount(Long.parseLong(channelId));
	if("init".equals(method)){ 
		count=0;
		
	}
%>

<form action="./CrawlResourceServlet?method=list" method="post">
	标题<input type="text" value="<%=title %>" name="title"><input type="submit" value="查询">
	<input type="text" value="<%=pageSize %>" id="pageSize" name="pageSize">
	<input type="hidden" id="channelId" name="channelId" value="<%=channelId %>">
	<!-- 测试用 -->
	<input type="text" value="<%=count %>" id="test">
</form>
<table>
	
	<tr>
		<td>编号</td>
		<td>状态</td>
		<td>标题</td>
		<td>时间</td>
	</tr>

<c:if test="<%=(count >0) %>">
<pg:pager items="<%= count %>"
	url="./CrawlResourceServlet" maxIndexPages="5"
	export="currentPageNumber=pageNumber"
	scope="request">
	<pg:param name="method" value="list" />
	<pg:param name="channelId" value="<%=channelId %>" />
	<pg:param name="pageSize" value="<%=pageSize %>"/>
	
	<c:forEach var="res" items="<%=list %>">
	<tr id="${res.resId }">
		<td>${res.resId }</td>
		<td>${res.status }</td>
		<td>${res.title }</td>
		<td>${res.createTime }</td>
	</tr>	
	</c:forEach>
	
	<pg:index>

		<pg:first>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[首页 ]</a>
		</pg:first>

		<pg:prev>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[上页 ]</a>
		</pg:prev>

		<pg:pages>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>"><%=pageNumber%></a>
		</pg:pages>

		<pg:next>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[下页]</a>
		</pg:next>

		<pg:last>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[末页]</a>
		</pg:last>

	</pg:index>
</pg:pager>
</c:if>

</table>



</body>
</html>