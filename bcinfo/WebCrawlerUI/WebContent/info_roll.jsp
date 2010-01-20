<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Roll Info</title>
</head>
<body>
<div style="width: 100%; overflow: hidden;" onMouseOver= clearInterval(timer1);; onMouseOut = go();;>
	<div style="position: relative; top: 0px; left: 0px; white-space: nowrap; color: #0000FF;" id="news">
		<span id="headnew"> 珍爱生命远离低俗! </span>
		<script language="javascript">
			document.write(" 请谨慎发布内容! "); //新闻内容
			document.write(" 你会是下一个20万罚款得主吗?!!! "); //新闻内容
		</script>
	</div>
</div>
<script language=javascript>
	function newsScroll() {
		news.style.pixelLeft = (news.style.pixelLeft - 1) % headnew.offsetWidth;//实现不间断滚动
	}

	function go() {
		timer1 = setInterval('newsScroll()', 100); //更改第二个参数可以改变速度，值越小，速度越快。
	}
	go();
</script>
</body>
</html>