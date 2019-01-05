<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String webCtx = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="<%=webCtx %>/static/easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=webCtx %>/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=webCtx %>/static/easyui/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="<%=webCtx %>/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="<%=webCtx %>/static/easyui/themes/icon.css"/>
<link rel="stylesheet" href="<%=webCtx %>/static/easyui/myicon.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script>
	function reloadtree()
	{
		$.get('/modulefuncroot.action',function(data)
			{
				$('#tree').tree({
					data: data
				});
			}
		)
	}
	function reloadroletree()
	{
		$.get('/roleroot.action',function(data)
			{
				$('#roletree').tree({
					data: data
				});
			}
		)
	}
	function addperm()
	{
		var nodes = $('#tree').tree('getChecked');
		if(nodes.length == 0)
		{
			$.messager.alert('提示','请选择模块或者权限节点,选择模块节点时将授权此模块下所有权限');
			return;
		}
		var permId = '';
		for(var i=0;i<nodes.length;i++)
		{
			permId = permId + nodes[i].id + ',';
		}
		var nodes2 = $('#roletree').tree('getChecked');
		if(nodes2.length == 0)
		{
			$.messager.alert('提示','请选择角色节点');
			return;
		}
		var roleId = '';
		for(var i=0;i<nodes2.length;i++)
		{
			roleId = roleId + nodes2[i].id + ',';
		}
		console.log('roleid='+roleId+',permid='+permId);
		$.ajax({
			url:'/modulerolesave.action?permId='+permId+"&roleId="+roleId,
			success:function(data)
			{
				$('#permdg').datagrid('reload');
			}
		})
	}
	function deleteperm()
	{
		var objs = $('#permdg').datagrid('getChecked');
		var id = '';
		for(i=0;i<objs.length;i++)
			id = id + objs[i].id+ ',';
		console.log('deleteperm id='+id);
		$.ajax({
			url:'/moduleroledelete.action?id='+id,
			success:function(data)
			{
				$('#permdg').datagrid('reload');
			}
		})
	}
	$(document).ready(function()
	{
		reloadtree();
		$('#tree').tree({
			onBeforeExpand:function(node,param)
			{
				if(node.attributes.isload=='false')
				{
					$.ajaxSettings.async = false;
					var url = '/modulegettreebyid.action?id='+node.id;
					$.get(url,function(data)
						{
							$('#tree').tree('append', {
								parent: node.target,
								data: data
							});
						}
					)
					$.ajaxSettings.async = true;
					node.attributes.isload = 'true';
				}
			},
			onExpand: function(node){

			},
			onClick: function(node){
				$('#permdg').datagrid({
					url:'/moduleroleload.action?funcId='+node.id
				});
			}
		});
		$('#roletree').tree({
			onClick: function(node){
				$('#permdg').datagrid({
					url:'/moduleroleload.action?roleId='+node.id
				});
			}
		});
		reloadroletree();
	});
</script>

</head>
<body class="easyui-layout">
	<div data-options="region:'west',title:'权限导航'" style="width:200px;padding:0px;">
		<ul id="tree" class="easyui-tree" data-options="checkbox:true,cascadeCheck:false,lines:true">   
		</ul> 
	</div>
	<div data-options="region:'center'">
		<div id="p" class="easyui-panel" style="width:100%;height:100%" data-options="iconCls:'icon-save',closable:true">
			<div class="easyui-layout" data-options="fit:true"> 
				<div data-options="region:'west',title:'角色导航'" style="width:200px;padding:0px;">
					<ul id="roletree" class="easyui-tree" data-options="checkbox:true,cascadeCheck:false,lines:true">   
					</ul> 
				</div>
				<div data-options="region:'center',title:'权限管理'">
					<table id="permdg" class="easyui-datagrid" style="width:100%;height:100%"   
					        data-options="url:'/moduleroleload.action',fitColumns:true,pagination:true,
					        pageSize:10,pageList:[10,50,100,200],toolbar:'#tb'">   
					    <thead>   
					        <tr>
					        	<th data-options="field:'id',width:100,checkbox:true">ID</th>   
					            <th data-options="field:'funcName',width:100">模块名称</th>   
					            <th data-options="field:'name',width:100">权限名称</th>
					            <th data-options="field:'permission',width:100">权限代码</th>
					            <th data-options="field:'roleName',width:100">授权角色</th>
					        </tr>   
					    </thead>   
					</table>
					<div id="tb">
						<a id="btn" onclick="addperm()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加授权</a>
						<a id="btn" onclick="deleteperm()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除授权</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
