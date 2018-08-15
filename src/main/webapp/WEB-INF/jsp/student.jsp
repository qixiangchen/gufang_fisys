<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生列表</title>
<link rel="stylesheet" type="text/css" href="/js/base.css">
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script>
	function doadd()
	{
		window.location.href="/add"
	}
	function doview(id)
	{
		var url = '/view?id='+id;
		window.open(url,'_blank');
	}
</script>
</head>
<body>
	<input type="button" value="添加" onclick="doadd()"/>
	<table border=1>
		<tr>
			<th>全选</th>
			<th>编号</th>
			<th>姓名</th>
			<th>年级</th>
			<th>兴趣</th>
			<th>操作</th>
		</tr>
		<c:forEach items="${students}" var="s">
		<tr>
			<td><input type="checkbox" id="id" name="id" value="${s.id }"/></td>
			<td>${s.id }</td>
			<td>${s.name }</td>
			<td>${s.grade }</td>
			<td>${s.hname }</td>
			<td><input type="button" value="查看" onclick="doview('${s.id }')"></td>
		</tr>
		</c:forEach>
	</table>
	${pageDiv }
</body>
</html>