<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Date"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>DongQi</title>
</head>
<body>
dongq<br />
<%=new Date() %><br />
<%
Class.forName("oracle.jdbc.driver.OracleDriver");
Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.202:1521:ora92","scwap","hello520tshirt");
PreparedStatement pst = conn.prepareStatement("select * from wap_filter");
ResultSet rs = pst.executeQuery();
while(rs.next()){
	out.println(rs.getLong(1)+"|"+rs.getString(2)+"<br />");
}
conn.close();
%>
</body>
</html>