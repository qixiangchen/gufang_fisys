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
	function dosave()
	{
		var data = $('#addform').serialize();
		alert(data);
		$.ajax({
			url:'/save',
			data:data,
			success:function(result)
			{
				if(result)
				{
					alert('保存成功');
					window.location.href='/list';
					//window.open("/list","_self");
				}
				else
				{
					alert('保存失败');
				}
			}
		})
	}
	function dolist()
	{
		window.location.href='/list';
	}
</script>
</head>
<body>
	<form id="addform" action="/save" method="post">
	<table border=1>
		<tr>
			<td>名称</td>
			<td><input type="text" id="name" name="name" value="${stu.name }"></td>
		</tr>
		<tr>
			<td>年级</td>
			<td><input type="text" id="grade" name="grade" value="${stu.grade }"></td>
		</tr>
		<tr>
			<td>兴趣</td>
			<td>
			<c:forEach items="${hobby}" var="h">
				<input type="checkbox" id="hid" name="hid" value="${h.id }" ${h.checked}>${h.name }
			</c:forEach>
			</td>
		</tr>
	</table>
<%
	String view = (String)request.getAttribute("view");
	if(!"true".equals(view))
	{
%>
	<input type="button" value="保存" onclick="dosave()"/>
<%
	}
%>
	<input type="button" value="返回列表" onclick="dolist()"/>
	</form>
</body>
</html>