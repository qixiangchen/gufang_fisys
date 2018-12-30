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
function addsuborg()
{
	var objs = $('#tree').tree('getChecked');
	if(objs.length != 1)
	{
		$.messager.alert('错误','请选中一个父节点');
		return;
	}
	var id = objs[0].id;
	$('#parentId').val(id);
	$('#frm').form('submit', {    
	    url:'/orgsubsave',    
	    onSubmit: function(){    
	          
	    },    
	    success:function(data){    
	    	reloadtree();
	    }    
	}); 
}
function updatesuborg()
{
	$('#frm').form('submit', {    
	    url:'/orgsave',    
	    onSubmit: function(){    
	          
	    },    
	    success:function(data){    
	    	reloadtree();
	    }    
	}); 
}
function deleteorg()
{
	var objs = $('#tree').tree('getChecked');
	if(objs.length == 0)
	{
		$.messager.alert('错误','请选中一个需要删除部门');
		return;
	}
	var id = '';
	for(var i=0;i<objs.length;i++)
	{
		id = id + objs[i].id + ',';
	}
	console.log('deleteorg()='+objs.length+','+id);
	$.ajax({
		url:'/orgdelete?id='+id,
		success:function(data)
		{
			$.messager.alert('信息',data);
			reloadtree();
		}
	})
}
function reloadtree()
{
	//$('#tree').tree('loadData',[{"id":"1","text":"Root","iconCls":null,"url":null,"state":"closed","attributes":{"type":"system","isload":"false","path":"/","parentId":null}}]);
	$.get('/orgroot',function(data)
		{
			$('#tree').tree({
				data: data
			});
		}
	)
}
$(document).ready(
	function()
	{
		//加载树控件根节点
		reloadtree();
		//设计树控件事件响应规则，当点击树的某节点时执行这些方法
		$('#tree').tree({
			onBeforeExpand:function(node,param)
			{
				if(node.attributes.isload=='false')
				{
					$.ajaxSettings.async = false;
					var url = '/orgchild?id='+node.id;
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
			onClick: function(node){
				$('#id').val(node.id);
				$('#name').textbox('setValue',node.text);
				$('#seqno').textbox('setValue',node.attributes.seqno);
				$('#parentId').val(node.attributes.parentId);
				$('#orgPath').textbox('setValue',node.attributes.orgPath);
				$('#fullName').textbox('setValue',node.attributes.fullName);
			}
		});
	}
);		
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'自定义导航'" style="width:250px;">
		<ul id="tree" class="easyui-tree" data-options="lines:true,checkbox:true,cascadeCheck:false">   
		</ul> 
    </div>
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
				<a id="btn" onclick="addsuborg()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">添加子部门</a>  
				<a id="btn" onclick="updatesuborg()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">修改部门</a>  
				<a id="btn" onclick="deleteorg()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">删除部门</a>  
		    </div>
		</form>
    </div>
</body>
</html>