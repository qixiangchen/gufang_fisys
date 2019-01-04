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

function addrole()
{
	$('#win').window('open');
}
function closerole()
{
	$('#win').window('close');
}
function saverole()
{
	var rtn = $('#frm').form('validate');
	if(rtn)
	{
		$('#frm').form('submit', {    
		    url:'/rolesave.action',    
		    onSubmit: function(){    
	  
		    },    
		    success:function(data){    
		    	$('#roledg').datagrid('reload');
		    	closerole();
		    }    
		});
	}
}
function deleterole()
{
	var objs = $('#roledg').datagrid('getChecked');
	var id = '';
	for(i=0;i<objs.length;i++)
		id = id + objs[i].id+ ',';
	$.ajax({
		url:'/roledelete.action?id='+id,
		success:function(data)
		{
			$('#roledg').datagrid('reload');
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
    <div data-options="region:'center',title:'角色维护'" style="padding:5px;">
		<table id="roledg" class="easyui-datagrid" style="width:100%;height:100%"   
		        data-options="url:'/roleload.action',fitColumns:true,pagination:true,
		        pageSize:10,pageList:[10,50,100,200],toolbar:'#tb'">   
		    <thead>   
		        <tr>
		        	<th data-options="field:'id',width:100,checkbox:true">ID</th>   
		            <th data-options="field:'name',width:100">姓名</th>   
		            <th data-options="field:'description',width:100">描述</th>
		        </tr>   
		    </thead>   
		</table>
		<div id="tb">
			<a id="btn" onclick="addrole()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加角色</a>
			<a id="btn" onclick="deleterole()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除角色</a>
		</div>

		<div id="win" class="easyui-window" title="角色窗口" style="width:400px;height:280px"   
		        data-options="iconCls:'icon-save',modal:true">   
		    <div class="easyui-layout" data-options="fit:true">      
		        <div data-options="region:'center'">   
					<form id="frm" method="post">
						<input type="hidden" id="id" name="id"/>
						<input type="hidden" id="flag" name="flag"/>
					    <div style="margin-left:50px;margin-top:10px">      
					        <input class="easyui-textbox" type="text" id="name" name="name" data-options="width:250,label:'姓名:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">    
					        <input class="easyui-textbox" type="text" id="description" name="description" data-options="width:250,label:'描述:'" />
					    </div>
			  		    <div style="margin-left:50px;margin-top:30px"> 
							<a id="btn" onclick="saverole()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">保存</a>  
							<a id="btn" onclick="closerole()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">关闭</a>    
					    </div>
					</form>
		        </div>   
		    </div>   
		</div>  
    </div>
</body>
</html>