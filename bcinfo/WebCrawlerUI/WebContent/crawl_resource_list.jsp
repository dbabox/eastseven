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
	//height: 530,width: 500
	var config = { 
			buttons: { "关闭": function() { $(this).dialog("close"); }
						//"审核通过": function(){},
						//"审核未通过": function(){},
						,"查看原网页": function(){
							var link = $("#dialog_link").html();
							//alert($("#dialog_link").html());
							//window.location.replace($("#dialog_link").html());
							window.open(link);
						}//window.location.replace(link);
		 	},
			autoOpen: false,
			modal: true,
			width: 500,
			position: 'top'
	};
	
	$(document).ready(function() {
		
		//绑定按钮事件
		$("#sendBtn").bind('click',function(){
			var channelId = $("#channelId").val();
			var ids = new Array();
			var jsonText = '';
			$(":checkbox").each(function(i){
				var obj = $(this).attr("checked");
				
				if(obj){
					//alert($(this).val());
					ids[i] = $(this).val();
					jsonText += $(this).val()+",";
				}
				
			});
			//发送ajax请求
			$.get('./CrawlResourceServlet?method=send',{channelId:channelId,resIds:jsonText},function(data){
				var json = eval('('+data+')');
				var msg = json.msg;
				alert(msg);
				
			});
			for(var i=0;i<ids.length;i++){
				$("#id_"+ids[i]).attr("checked",false);	
			}
		});
		
		//初始化
		$("#dialog").dialog(config);

		//给所有tr td 绑定一个dialog
		$("tr td:first-child").bind('click',function(){
			//初始化dialog中的内容
			$("#dialog").empty();
			$("#dialog_link").empty();
			var res_id = $(this).attr("id");
			
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
<div id="dialog_link" style="display:none"></div>
<%
	String pageSize = (request.getAttribute("pageSize")!=null)?(String)request.getAttribute("pageSize"):"15";
	String channelId = (String) request.getParameter("channelId");
	String title = (String) request.getParameter("title");
	if(title == null) title = "";
	if(channelId == null || "".equals(channelId)) channelId = (String)request.getAttribute("channelId");
%>
<%
	List<CrawlResource> list = (List<CrawlResource>) request.getAttribute("crawlResourceList");
	int count = new CrawlResourceDao().getCount(Long.parseLong(channelId));
%>

<form id="crawlResourceMainForm" action="./CrawlResourceServlet?method=list" method="post">
	标题
	<input type="text" value="<%=title %>" id="title" name="title"/>
	<input type="submit" value="查询"/>
	<select name="resStatus">
		<option value="-1" selected="selected">全部</option>
		<option value="1">已审</option>
		<option value="0">未审</option>
	</select>
	<input type="text" value="<%=pageSize %>" id="pageSize" name="pageSize"/>
	
	<input type="hidden" name="channelId" value="<%=channelId %>"/>
	<!-- 测试用 -->
	<input type="hidden" value="<%=count %>" id="test"/>
</form>

<form id="tableForm" action="./CrawlResourceServlet?method=update" method="post">
<input type="submit" value="审核"/>
<input type="button" id="sendBtn" value="发送"/>
<input type="hidden" id="channelId" name="channelId" value="<%=channelId %>"/>
<table>
	
	<tr>
		<td>序号</td>
		<td>编号</td>
		<td>操作<input type="hidden" id="checkBox"/></td>
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
	
	
	<c:forEach var="res" items="<%=list %>" varStatus="status">
	<tr>
		<td id="${res.resId }">${status.index+1 }</td>
		<td>${res.resId }</td>
		<td><input type="checkbox" id="id_${res.resId }" name="checkStatus" value="${res.resId }"/></td>
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
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[末页][共<%=count %>条记录]</a>
		</pg:last>

	</pg:index>
</pg:pager>
</c:if>

</table>
</form>


</body>
</html>