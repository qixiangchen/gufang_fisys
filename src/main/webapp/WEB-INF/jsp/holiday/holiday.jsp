<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
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
//nexttask 工作流下一办理环节ID
//nextuserid 工作流下一办理环节审批人  loginId,userName
function wfsubmit(nexttask,nextuserid)
{
	var rtn = $('#frm').form('validate');
	if(!rtn)
		return;
	var userId = '';
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
			userId = userId + objdim2[0] + ',';
		}
	}
	var rtn = $('#frm').form('validate');
	if(rtn)
	{
		$('#nexttask').val(nexttask);
		$('#nextUserId').val(userId);
		$('#frm').form('submit', {    
		    url:'/holidaysubmit.action',    
		    onSubmit: function(){    
	  
		    },    
		    success:function(data){
		    	alert(data);
				$('#btndiv').empty();
		    }
		});
	}
}
</script>

</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'请假单'">
		<form id="frm" method="post">
			<input type="hidden" id="id" name="id" value="${hdi.id}"/>
			<input type="hidden" id="userId" name="userId"/>
			<!-- 工作流控制数据 -->
			<input type="hidden" id="nexttask" name="nexttask"/>
			<input type="hidden" id="nextUserId" name="nextUserId"/>
			<!-- 工作流实例数据 -->
			<input type="hidden" id="instprocessid" name="instprocessid" value="${instprocessid}"/>
			<input type="hidden" id="instactivityid" name="instactivityid" value="${instactivityid}"/>
			<input type="hidden" id="instworkitemid" name="instworkitemid" value="${instworkitemid}"/>
			
		    <div style="margin-left:50px;margin-top:30px">   
		        <input class="easyui-datebox" type="text" id="createDt" name="createDt" value="${hdi.createDt}" data-options="label:'请假开始日期:',width:300,required:true" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="days" name="days" value="${hdi.days}" data-options="label:'请假天数:',width:300,required:true,validType:'number'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="reason" name="reason" value="${hdi.reason}" data-options="label:'请假原因:',width:300,required:true" />   
		    </div>
  		    <div id="btndiv" style="margin-left:50px;margin-top:30px">
<%
List<Properties> nextBtnList = (List<Properties>)request.getAttribute("button");
for(Properties prop:nextBtnList)
{
	System.out.println(prop);
	String js = prop.getProperty("js");
	String name = prop.getProperty("name");
%>  		    
				<a id="btn" onclick="<%=js %>" class="easyui-linkbutton" data-options="iconCls:'icon-search'"><%=name %></a>
<%
}
%>
		    </div>
		</form>
	</div>
</body>
</html>
