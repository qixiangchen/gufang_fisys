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
function openitem(index, row)
{
	var url = row.url+'?instanceid='+row.instanceId+'&instworkitemid='+row.id+'&instactivityid='+row.instActivityId+'&instprocessid='+row.instProcessId;
	var h = $(window.parent).height() - 125;
	var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:'+h+'px;"></iframe>';
	window.parent.$('#tabui').tabs('add',{
		title:row.title,
		content:content,
		closable:true,
		iconCls:'icon'
	});
}
$(document).ready(
		function()
		{			
			$('#workitemdg').datagrid({
				'onClickRow':openitem
			})
		}
);
</script>

</head>
<body class="easyui-layout">
	<div data-options="region:'center'">
		<table id="workitemdg" class="easyui-datagrid" style="width:100%;height:100%"   
		        data-options="url:'/workitemlist.action',fitColumns:true">   
		    <thead>   
		        <tr>
		        	<th data-options="field:'id',width:100,hidden:true">ID</th>   
		            <th data-options="field:'processName',width:100">流程名称</th>   
		            <th data-options="field:'startUserIdName',width:100">流程启动人</th>   
		            <th data-options="field:'userIdName',width:100">审批人</th>
		            <th data-options="field:'title',width:100">标题</th>   
		            <th data-options="field:'statusName',width:100">审批环节</th>
		            <th data-options="field:'instProcessId',width:100,hidden:true">instProcessId</th>
		            <th data-options="field:'instActivityId',width:100,hidden:true">instActivityId</th>
		            <th data-options="field:'url',width:100,hidden:true">Url</th>
		        </tr>   
		    </thead>   
		</table>
	</div>
</body>
</html>
