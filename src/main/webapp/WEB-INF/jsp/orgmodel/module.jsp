<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="/static/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/static/easyui/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="/static/easyui/themes/icon.css"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务报销系统</title>
<script>

function addperm()
{
	$('#win').window('open');
}
function closeperm()
{
	$('#win').window('close');
}
function saveperm()
{
	var rtn = $('#frm').form('validate');
	if(rtn)
	{
		$('#frm').form('submit', {    
		    url:'/modulesave.action',    
		    onSubmit: function(){    
	  
		    },    
		    success:function(data){    
		    	$('#permdg').datagrid('reload');
		    	closeperm();
		    }    
		});
	}
}
function deleteperm()
{
	var objs = $('#permdg').datagrid('getChecked');
	var id = '';
	for(i=0;i<objs.length;i++)
		id = id + objs[i].id+ ',';
	$.ajax({
		url:'/moduledelete.action?id='+id,
		success:function(data)
		{
			$('#permdg').datagrid('reload');
		}
	})
}

$(document).ready(
		function()
		{
			$('#win').window('close');
		}
	);		
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'center',title:'模块维护'" style="padding:5px;">
		<table id="permdg" class="easyui-datagrid" style="width:100%;height:100%"   
		        data-options="url:'/moduleload.action',fitColumns:true,pagination:true,
		        pageSize:10,pageList:[10,50,100,200],toolbar:'#tb'">   
		    <thead>   
		        <tr>
		        	<th data-options="field:'id',width:100,checkbox:true">ID</th>   
		            <th data-options="field:'moduleName',width:100">模块名称</th>   
		            <th data-options="field:'name',width:100">权限名称</th>
		            <th data-options="field:'permission',width:100">权限代码</th>
		        </tr>   
		    </thead>   
		</table>
		<div id="tb">
			<a id="btn" onclick="addperm()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加权限</a>
			<a id="btn" onclick="deleteperm()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除权限</a>
		</div>

		<div id="win" class="easyui-window" title="模块权限窗口" style="width:400px;height:280px"   
		        data-options="iconCls:'icon-save',modal:true">   
		    <div class="easyui-layout" data-options="fit:true">      
		        <div data-options="region:'center'">   
					<form id="frm" method="post">
						<input type="hidden" id="id" name="id"/>
					    <div style="margin-left:50px;margin-top:10px">      
					        <input class="easyui-textbox" type="text" id="moduleName" name="moduleName" data-options="width:250,label:'模块名称:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">    
					        <input class="easyui-textbox" type="text" id="name" name="name" data-options="width:250,label:'权限名称:'" />
					    </div>
					    <div style="margin-left:50px;margin-top:10px">    
					        <input class="easyui-textbox" type="text" id="permission" name="permission" data-options="width:250,label:'权限代码:'" />
					    </div>
			  		    <div style="margin-left:50px;margin-top:30px"> 
							<a id="btn" onclick="saveperm()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">保存</a>  
							<a id="btn" onclick="closeperm()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">关闭</a>    
					    </div>
					</form>
		        </div>   
		    </div>   
		</div>  
    </div>
</body>
</html>