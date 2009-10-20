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
			buttons: { "�ر�": function() { $(this).dialog("close"); }
						//"���ͨ��": function(){},
						//"���δͨ��": function(){},
						,"�鿴ԭ��ҳ": function(){
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
		
		//�󶨰�ť�¼�
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
			//����ajax����
			$.get('./CrawlResourceServlet?method=send',{channelId:channelId,resIds:jsonText},function(data){
				var json = eval('('+data+')');
				var msg = json.msg;
				alert(msg);
				
			});
			for(var i=0;i<ids.length;i++){
				$("#id_"+ids[i]).attr("checked",false);	
			}
		});
		
		//��ʼ��
		$("#dialog").dialog(config);

		//������tr td ��һ��dialog
		$("tr td:first-child").bind('click',function(){
			//��ʼ��dialog�е�����
			$("#dialog").empty();
			$("#dialog_link").empty();
			var res_id = $(this).attr("id");
			
			//����ajax����
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
	����
	<input type="text" value="<%=title %>" id="title" name="title"/>
	<input type="submit" value="��ѯ"/>
	<select name="resStatus">
		<option value="-1" selected="selected">ȫ��</option>
		<option value="1">����</option>
		<option value="0">δ��</option>
	</select>
	<input type="text" value="<%=pageSize %>" id="pageSize" name="pageSize"/>
	
	<input type="hidden" name="channelId" value="<%=channelId %>"/>
	<!-- ������ -->
	<input type="hidden" value="<%=count %>" id="test"/>
</form>

<form id="tableForm" action="./CrawlResourceServlet?method=update" method="post">
<input type="submit" value="���"/>
<input type="button" id="sendBtn" value="����"/>
<input type="hidden" id="channelId" name="channelId" value="<%=channelId %>"/>
<table>
	
	<tr>
		<td>���</td>
		<td>���</td>
		<td>����<input type="hidden" id="checkBox"/></td>
		<td>״̬</td>
		<td>����</td>
		<td>ʱ��</td>
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
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[��ҳ ]</a>
		</pg:first>

		<pg:prev>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[��ҳ ]</a>
		</pg:prev>

		<pg:pages>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>"><%=pageNumber%></a>
		</pg:pages>

		<pg:next>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[��ҳ]</a>
		</pg:next>

		<pg:last>
			<a href="<%= pageUrl %>&pageNo=<%= pageNumber %>">[ĩҳ][��<%=count %>����¼]</a>
		</pg:last>

	</pg:index>
</pg:pager>
</c:if>

</table>
</form>


</body>
</html>