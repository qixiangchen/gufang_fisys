<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
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
	//alert(nexttask+'---'+userId);
	var rtn = $('#frm').form('validate');
	if(rtn)
	{
		$('#nexttask').val(nexttask);
		$('#nextUserId').val(userId);
		$('#frm').form('submit', {    
		    url:'/expsubmit.action',    
		    onSubmit: function(){    
	  
		    },    
		    success:function(data){
		    	alert(data);
		    	alert(data.instProcessId);
		    	$('#buttondiv').empty();
		    	loadimg(data.instProcessId);
		    }
		});
	}
}
function loadimg(instprocessid)
{
	var url = $("#wfuiimg").attr('src');
	$("#wfuiimg").attr('src', url+'&instprocessid='+instprocessid+'&r='+ Math.random());
}
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'center',title:'部门维护'" style="padding:5px;">
    	<div >
    		<img id="wfuiimg" height="100" width="800" src="/downimg.jsp?width=800&height=100&processid=expense&instprocessid=${instprocessid}&instanceid=${eps.id}&t=<%=new Date()%>"/>
    	</div>
		<form id="frm" method="post">
			<input type="hidden" id="instprocessid" name="instprocessid" value="${instprocessid}"/>
			<input type="hidden" id="instactivityid" name="instactivityid" value="${instactivityid}"/>
			<input type="hidden" id="instworkitemid" name="instworkitemid" value="${instworkitemid}"/>
			<input type="hidden" id="id" name="id" value="${eps.id}"/>
			<input type="hidden" id="nexttask" name="nexttask"/>
			<input type="hidden" id="nextUserId" name="nextUserId"/>
			<input type="hidden" id="userId" name="userId" value="${eps.userId}"/>
		    <div style="margin-left:50px;margin-top:30px">   
		        <input class="easyui-textbox" type="text" id="name" name="name" value="${eps.name}" data-options="width:300,label:'请款名称:'" />   
		    </div>   
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="company" name="company" value="${eps.company}" data-options="width:300,label:'公司名称:'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="bank" name="bank" value="${eps.bank}" data-options="width:300,label:'银行名称:'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="bankAccount" name="bankAccount" value="${eps.bankAccount}" data-options="width:300,label:'银行账号:'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="amount" name="amount" value="${eps.amount}" data-options="width:300,label:'金额(单位:分):'" />   
		    </div>
  		    <div id="buttondiv" style="margin-left:50px;margin-top:30px">
<%
			List<Properties> button = (List<Properties>)request.getAttribute("button");
			for(Properties prop:button)
			{
				String name = prop.getProperty("name");
				String js = prop.getProperty("js");
%>
				<a id="btn" onclick="<%=js %>" class="easyui-linkbutton"><%=name %></a>
<%
			}
%>  
		    </div>
		</form>
    </div>
</body>
</html>