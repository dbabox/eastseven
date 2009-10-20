<%@ Language=VBScript codepage=936  %>
<%  Option Explicit %>
<%
  Dim Conn, rs
  Set Conn  = Server.CreateObject("ADODB.Connection")
  Set rs    = Server.CreateObject("ADODB.Recordset")

  Conn.Open  "Provider=Microsoft.Jet.OLEDB.4.0; Data Source="& Server.MapPath("./") &"\tree.mdb;"
  rs.open "Select ID, parentId, text, hint, icon, data, url, target, method From treeview order by ID", Conn
%>
<html>
  <head>
    <title>梅花雪中文网 - 网页脚本控件集 MzTreeView10</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <meta name="author" content="黄方荣(meizz·梅花雪)://www.meizz.com">
    <script language="JavaScript" src="MzTreeView10.js"></script>
    <link href="http://www.meizz.com/Scripts/Global.css" type="text/css" rel="stylesheet">
    <style>
    A.MzTreeview
    {
      font-size: 9pt;
      padding-left: 3px;
    }
    </style>
  </head>

  <body class=frame>
    <SCRIPT LANGUAGE="JavaScript">
    <!--
    window.tree = new MzTreeView("tree");

    tree.icons["property"] = "property.gif";
    tree.icons["css"] = "collection.gif";
    tree.icons["book"]  = "book.gif";
    tree.iconsExpand["book"] = "bookopen.gif"; //展开时对应的图片
    tree.setIconPath("http://www.meizz.com/Icons/TreeView/"); //可用相对路径
<%
  Dim node
  Dim reg : set reg = new RegExp : reg.global=True : reg.pattern=";"
  Dim id, parentId, text, hint, icon, data, url, target, method
  do while not rs.eof
    '若是树的条目比较多的时候(比如大于1000)而又相对稳定的时候将这些数据生成静态网页来访问
    node = VBCrLf &"    tree.nodes["""& rs("parentId") &"_"& rs("id") &"""] = """
    node = node &"text:"& reg.replace(rs("text"), chr(15)) &";"
    if rs("hint")<>"" then node = node &"hint:"& reg.replace(rs("hint"), chr(15)) &";"
    if rs("icon")<>"" then node = node &"icon:"& rs("icon") &";"
    if rs("data")<>"" then node = node &"data:"& reg.replace(rs("data"), chr(15)) &";"
    if rs("url")<>""  then node = node &"url:"&  reg.replace(rs("url"), chr(15)) &";"
    if rs("target")<>"" then node = node &"target:"& rs("target") &";"
    if rs("method")<>"" then node = node &"method:"& reg.replace(rs("method"), chr(15)) &";"
    response.write node &"""" '生成节点信息
    rs.movenext
  loop
%>
    tree.setURL("Catalog.asp");
    tree.setTarget("MzMain");
    document.write(tree.toString());    //亦可用 obj.innerHTML = tree.toString();
    //-->
    </SCRIPT>
  </body>
</html>
<%
  rs.close : set rs = nothing : Conn.close : set Conn = nothing
%>