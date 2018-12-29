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
	alert(userid);
}
$(document).ready(
		function()
		{
			reloadtree();
			$('#win').window('close');
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
		            <th data-options="field:'address',width:100">地址</th>
		            <th data-options="field:'companyMail',width:100">公司邮箱</th>  
		            <th data-options="field:'privateMail',width:100">私人邮箱</th>  
		            <th data-options="field:'companyTeleNo',width:100">公司电话</th>
		            <th data-options="field:'homeTeleNo',width:100">家庭电话</th>
		            <th data-options="field:'mobile',width:100">手机</th>
		            <th data-options="field:'title',width:100">职位</th>
		            <th data-options="field:'desc',width:100,hidden:true">ID</th>
		            <th data-options="field:'orgId',width:100,hidden:true">ID</th>
		            <th data-options="field:'orgPath',width:100,hidden:true">ID</th>
		            <th data-options="field:'enabled',width:100,hidden:true">ID</th>
		            <th data-options="field:'locked',width:100,hidden:true">ID</th>
		            <th data-options="field:'managerId',width:100,hidden:true">ID</th>
		            <th data-options="field:'birthday',width:100,hidden:true">ID</th>
		            <th data-options="field:'cardId',width:100,hidden:true">ID</th>
		            <th data-options="field:'failureDate',width:100,hidden:true">ID</th>
		            <th data-options="field:'failureCount',width:100,hidden:true">ID</th>
		            <th data-options="field:'openid',width:100,hidden:true">ID</th>
		            <th data-options="field:'seqno',width:100,hidden:true">ID</th>
		            <th data-options="field:'flag',width:100,hidden:true">ID</th>
		            <th data-options="field:'testMode',width:100,hidden:true">ID</th>
		        </tr>   
		    </thead>   
		</table>
		<div id="tb">
			<input class="easyui-textbox" type="text" id="qname" name="qname" data-options="width:250,label:'输入用户名称:'" />
			<a id="btn" onclick="queryuser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a id="btn" onclick="saveroleuser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">保存</a>
		</div>
    </div>
</body>
</html>