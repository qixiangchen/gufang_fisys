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
<link rel="stylesheet" href="<%=webCtx %>/static/js/login.css"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>古方红糖财务管理系统</title>
<script>
function login(){
	var frmdata = $('#loginForm').serialize();
	$.post('/logindone',frmdata,function(data){
		if(data == 'true')
		{
			window.open('/main.action','_self');
		}
		else
		{
			$('#msgdiv').html(data);
		}
	})
}
</script>
</head>

 <body>
 	<div class="login_panel">
    	<form id="loginForm" action="<%=webCtx %>/logindone" method="post">
        	<div class="logo"></div>
            <div class="sysname-zh">古方红糖财务管理系统</div>
            <div class="sysname-en" style="">GuFuang Sugar Financial Accounting System</div>
            <div id="msgdiv" class="message"></div>
            <table border="0" style="width:300px;">
            	<tr>
                	<td style="white-space:nowrap; padding-bottom: 5px;width:55px;">用户名：</td>
                    <td colspan="2">
                    <input type="text" id="loginId" name="loginId" class="login" 
                   	 value="${cookie.loginId.value}"/>
                   	 </td>
                </tr>
                <tr>
                    <td class="lable" style="white-space:nowrap; letter-spacing: 0.5em; vertical-align: middle">密码：</td>
                    <td colspan="2">
                    <input type="password" id="pwd" name="pwd" class="login"
                    	 value="${cookie.pwd.value}"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="2"><input type="checkbox"/><span>系统登录信息</span></td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align:center">
                        <input type="button" onclick="login()" value="登录" class="botton" />
                        <input type="button" value="重置" class="botton"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

</html>
