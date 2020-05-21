<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
	String menu = (String)request.getAttribute("menu");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/easyui/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="/easyui/themes/icon.css"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务报销系统</title>
<script>
function wfsubmit(nexttask,nextuserid)
{
	var rtn = $('#frm').form('validate');
	if(!rtn)
		return;
	var nextUserId = '';
	if('end' != nexttask && 'endall' != nexttask && 'destory' != nexttask)
	{
		if(nextuserid == null || nextuserid == '')
		{
			$.messager.alert('错误提示','下一办理人为空!','error');
			return;
		}

		var objdim = nextuserid.split(";");
		for(var k=0;k<objdim.length;k++)
		{
			var objdim2 = objdim[k].split(",");
			if(objdim2[0] == null || objdim2[0] == '')
			{
				$.messager.alert('错误提示','下一办理人为空!','error');
				return;
			}
			nextUserId = objdim2[0];
		}
	}
	frm.nexttask.value=nexttask;
	frm.userId.value=nextUserId;
	frm.operate.value='submit';
	frm.submit();
}
	
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'center',title:'部门维护'" style="padding:5px;">
		<form id="frm" method="post">
			<input type="hidden" id="id" name="id"/>
			<input type="hidden" id="parentId" name="parentId"/>
		    <div style="margin-left:50px;margin-top:30px">   
		        <input class="easyui-textbox" type="text" id="name" name="name" data-options="width:300,label:'部门名称:'" />   
		    </div>   
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="seqno" name="seqno" data-options="width:300,label:'部门优先级:'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="orgPath" name="orgPath" data-options="readonly:true,width:300,label:'部门路径:'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="fullName" name="fullName" data-options="readonly:true,width:300,label:'部门全称:'" />   
		    </div>

  		    <div style="margin-left:50px;margin-top:30px">
<%
			List<Properties> button = (List<Properties>)request.getAttribute("button");
			for(Properties prop:button)
			{
				String name = prop.getProperty("name");
				String js = prop.getProperty("js");
%>
				<a id="btn" onclick="<%=js %>" href="#" class="easyui-linkbutton"><%=name %></a>
<%
			}
%>  
		    </div>
		</form>
    </div>
</body>
</html>