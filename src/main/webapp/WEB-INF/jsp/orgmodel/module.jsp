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
	function addperm()
	{
		$('#id').val('');
		$('#funcId').val('');

		var nodes = $('#tree').tree('getChecked');
		if(nodes.length != 1)
		{
			$.messager.alert('提示','请选择唯一功能模块');
			return;
		}
		for(var i=0;i<nodes.length;i++)
		{
			funcid=nodes[i].id;
		}
		if(funcid == 'root')
		{
			$.messager.alert('提示','禁止选择根节点');
			return;
		}
		if(funcid.substr(0,4) == 'perm')
		{
			$.messager.alert('提示','请选择唯一功能模块,不能选择权限节点');
			return;
		}

		var rtn = $('#frm').form('validate');
		if(rtn)
		{
			$('#funcId').val(funcid);
			$('#frm').form('submit', {    
			    url:'/modulesave.action',    
			    onSubmit: function(){    
		  
			    },    
			    success:function(data){    
			    	reloadtree();
			    }    
			});
		}
	}
	function saveperm()
	{
		var funcid = $('#funcId').val();
		if(funcid == null || funcid =='')
		{
			var nodes = $('#tree').tree('getChecked');
			if(nodes.length != 1)
			{
				$.messager.alert('提示','请选择唯一功能模块');
				return;
			}
			for(var i=0;i<nodes.length;i++)
			{
				funcid=nodes[i].id;
			}
			if(funcid == 'root')
			{
				$.messager.alert('提示','禁止选择根节点');
				return;
			}
			if(funcid.substr(0,4) == 'perm')
			{
				$.messager.alert('提示','请选择唯一功能模块,不能选择权限节点');
				return;
			}
		}
		var rtn = $('#frm').form('validate');
		if(rtn)
		{
			$('#funcId').val(funcid);
			$('#frm').form('submit', {    
			    url:'/modulesave.action',    
			    onSubmit: function(){    
		  
			    },    
			    success:function(data){    
			    	reloadtree();
			    }    
			});
		}
	}
	function deleteperm()
	{
		var nodes = $('#tree').tree('getChecked');
		var id = '';
		for(var i=0;i<nodes.length;i++)
		{
			if(nodes[i].id.substr(0,4) == 'perm')
				id = id + nodes[i].id + ',';
		}
		$.ajax({
			url:'/moduledelete.action?id='+id,
			success:function(data)
			{
				reloadtree();
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
					//如果是点击末级权限节点,窗口显示信息,权限ID以perm为前缀
					if(node.id.substr(0,4) == 'perm')
					{
						$('#id').val(node.id);
						$('#name').textbox('setValue',node.text);
						$('#funcId').val(node.attributes.funcId);
						$('#permission').textbox('setValue',node.attributes.permission);
					}
				}
			})
		}
	);
</script>

</head>
<body class="easyui-layout">
	<div data-options="region:'west',title:'功能模块导航'" style="width:200px;padding:0px;">
		<ul id="tree" class="easyui-tree" data-options="checkbox:true,cascadeCheck:false,lines:true">   
		</ul> 
	</div>
	<div data-options="region:'center',title:'模块权限管理'">
		<form id="frm" method="post">
			<input type="hidden" id="id" name="id"/>
			<input type="hidden" id="funcId" name="funcId"/>
		    <div style="margin-left:50px;margin-top:30px">   
		        <input class="easyui-textbox" type="text" id="name" name="name" data-options="required:true,label:'权限名称:',width:300" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="permission" name="permission" data-options="required:true,label:'权限代码:',width:300" />   
		    </div>
  		    <div style="margin-left:50px;margin-top:30px">
  		    	<a id="btn" onclick="addperm()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">新增模块权限</a> 
				<a id="btn" onclick="saveperm()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存模块权限</a>  
				<a id="btn" onclick="deleteperm()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除模块权限</a>  
		    </div>
		</form>
	</div>
</body>
</html>
