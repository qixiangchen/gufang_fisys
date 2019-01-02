<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
function reloadtree()
{
	//$('#tree').tree('loadData',[{"id":"1","text":"Root","iconCls":null,"url":null,"state":"closed","attributes":{"type":"system","isload":"false","path":"/","parentId":null}}]);
	$.get('/roleroot',function(data)
		{
			$('#tree').tree({
				data: data
			});
		}
	)
}
function queryuser()
{
	var qname = $('#qname').val();
	$('#userdg').datagrid({
		url:'/userquery?qname='+qname
	})
}
function saveroleuser()
{
	var nodes = $('#tree').tree('getChecked');
	var roleid = '';
	for(var i=0;i<nodes.length;i++)
	{
		if('root' != nodes[i].id)
			roleid = roleid + nodes[i].id + ',';
	}
	var users = $('#userdg').datagrid('getChecked');
	var userid = '';
	for(var i=0;i<users.length;i++)
		userid = userid + users[i].id;
	$.ajax({
		url:'/roleassign?roleId='+roleid+"&userId="+userid,
		success:function(data)
		{
			queryuser();
		}
	})
}
function openuser(index, row)
{
	$('#win').window('open');
	var userId = row.id;
	$('#userroledg').datagrid({
		url:'/userrolequery?userId='+userId
	})
}
function deluserrole()
{
	var objs = $('#userroledg').datagrid('getChecked');
	var id = '';
	for(i=0;i<objs.length;i++)
	{
		id = id + objs[i].id + ',';
		userId = objs[i].userId;
	}
	$.ajax({
		url:'/userroledel?id='+id,
		success:function(data)
		{
			$('#userroledg').datagrid({
				url:'/userrolequery?userId='+userId
			})
		}
	})
}
$(document).ready(
		function()
		{
			reloadtree();
			$('#win').window('close');
			
			$('#userdg').datagrid({
				'onClickRow':openuser
			})
		}
	);		
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'角色树'" style="width:250px;">
		<ul id="tree" class="easyui-tree" data-options="lines:true,checkbox:true,cascadeCheck:false">   
		</ul> 
    </div>
    <div data-options="region:'center',title:'用户查询'" style="padding:5px;">
		<table id="userdg" class="easyui-datagrid" style="width:100%;height:250px"   
		        data-options="url:'/userquery',fitColumns:true,pagination:true,
		        pageSize:10,pageList:[10,50,100,200],toolbar:'#tb'">   
		    <thead>   
		        <tr>
		        	<th data-options="field:'id',width:100,checkbox:true">ID</th>   
		            <th data-options="field:'loginId',width:100">登录ID</th>   
		            <th data-options="field:'name',width:100">姓名</th>   
		            <th data-options="field:'mobile',width:100">手机</th>
					<th data-options="field:'roleName',width:100">已分配角色</th>
		        </tr>   
		    </thead>   
		</table>
		<div id="tb">
			<input class="easyui-textbox" type="text" id="qname" name="qname" data-options="width:250,label:'输入用户名称:'" />
			<a id="btn" onclick="queryuser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a id="btn" onclick="saveroleuser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">保存</a>
		</div>
		
		<div id="win" class="easyui-window" title="用户角色授权窗口" style="width:600px;height:320px"   
		        data-options="iconCls:'icon-save',modal:true">   
		    <div class="easyui-layout" data-options="fit:true">  
				<table id="userroledg" class="easyui-datagrid" style="width:100%;height:250px"   
				        data-options="url:'/userrolequery',fitColumns:true,pagination:true,
				        pageSize:10,pageList:[10,50,100,200]">   
				    <thead>   
				        <tr>
				        	<th data-options="field:'id',width:100,checkbox:true">ID</th>    
				            <th data-options="field:'name',width:100">姓名</th>
				            <th data-options="field:'userId',width:100">姓名</th>     
							<th data-options="field:'roleName',width:100">角色</th>
				        </tr>   
				    </thead>   
				</table>
				<a id="btn" onclick="deluserrole()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">删除</a>
				<a id="btn" onclick="closeuserrole()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">关闭</a>
			</div>
		</div>

    </div>
</body>
</html>