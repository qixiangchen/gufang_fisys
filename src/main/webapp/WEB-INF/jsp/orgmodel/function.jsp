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
		//$('#tree').tree('loadData',[{"id":"1","text":"Root","iconCls":null,"url":null,"state":"closed","attributes":{"type":"system","isload":"false","path":"/","parentId":null}}]);
		$.get('/funcroot.action',function(data)
			{
				$('#tree').tree({
					data: data
				});
			}
		)
	}
	function addsubfunc()
	{
		var isok = $('#frm').form('validate');
		if(!isok)
		{
			$.messager.alert('警告','数据校验失败'); 
			return;
		}
		$('#type').val('');
		var nodes = $('#tree').tree('getChecked');
		if(nodes.length!=1)
		{
			$.messager.alert('警告','请选择一个父节点'); 
			return;
		}
		var pid = nodes[0].id;
		$('#id').val(pid);
		var frmdata = $('#frm').serialize();
		$.ajax(
			{
				url:'/funcsubsave.action',
				data:frmdata,
				success:function(data){
					if(data == true)
					{
						reloadtree();
					}
					else
					{
						$.messager.alert('提示','请选择父节点');	
					}
				}
		})
	}
	function updatefunc()
	{
		var isok = $('#frm').form('validate');
		if(!isok)
		{
			$.messager.alert('警告','数据校验失败'); 
			return;
		}
		var frmdata = $('#frm').serialize();
		$.ajax(
			{
				url:'/funcsave.action',
				data:frmdata,
				success:function(data){
					if(data == true)
					{
						reloadtree();
					}
				}
		})
	}
	function deletefunc()
	{
		var frmdata = $('#frm').serialize();
		$.ajax(
			{
				url:'/funcdel.action',
				data:frmdata,
				success:function(data){
					if(data == false)
					{
						$.messager.alert('提示','系统功能模块禁止删除');
					}
					if(data == true)
					{
						reloadtree();
					}
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
						var url = '/gettreebyid.action?id='+node.id;
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
					$('#id').val(node.id);
					$('#name').textbox('setValue',node.text);
					$('#priority').textbox('setValue',node.attributes.priority);
					$('#parentId').val(node.attributes.parentId);
					$('#type').val(node.attributes.type);
					$('#url').textbox('setValue',node.url);
				}
			})
		}
	);
</script>

</head>
<body class="easyui-layout">
	<div data-options="region:'west',title:'功能模板导航'" style="width:200px;padding:0px;">
		<ul id="tree" class="easyui-tree" data-options="checkbox:true,cascadeCheck:false,lines:true">   
		</ul> 
	</div>
	<div data-options="region:'center',title:'模板管理'">
		<form id="frm" method="post">
			<input type="hidden" id="id" name="id"/>
			<input type="hidden" id="parentId" name="parentId"/>
			<input type="hidden" id="type" name="type"/>
		    <div style="margin-left:50px;margin-top:30px">   
		        <input class="easyui-textbox" type="text" id="name" name="name" data-options="label:'部门名称:',width:300" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="url" name="url" data-options="label:'功能URL:',width:300" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="priority" name="priority" data-options="label:'优先级:',width:300,required:true,validType:'number'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
				<select id="icon" name="icon" data-options="label:'图标:',width:300" class="easyui-combobox" name="dept" style="width:200px;">   
				    <option value="pic_1">pic_1</option>
				    <option value="pic_2">pic_2</option>
				    <option value="pic_3">pic_3</option>
				    <option value="pic_4">pic_4</option>
				    <option value="pic_5">pic_5</option>
				    <option value="pic_6">pic_6</option>
				    <option value="pic_7">pic_7</option>
				    <option value="pic_8">pic_8</option>
				    <option value="pic_9">pic_9</option>
				    <option value="pic_10">pic_10</option>
				    <option value="pic_11">pic_11</option>
				    <option value="pic_12">pic_12</option>
				    <option value="pic_13">pic_13</option>
				</select>
		    </div>

  		    <div style="margin-left:50px;margin-top:30px"> 
				<a id="btn" onclick="addsubfunc()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">添加子模块</a>  
				<a id="btn" onclick="updatefunc()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">修改模块</a>  
				<a id="btn" onclick="deletefunc()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">删除模块</a>  
		    </div>
		</form>
	</div>
</body>
</html>
