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

//当点击导航树的节点时，在办公区域显示页面
//此方法检查是否存在打开页面
var h = $(window).height() - 125;
function addTabCheckExist(title, url,icon){
	if ($('#tabui').tabs('exists', title)){
		$('#tabui').tabs('select', title);
	} else {
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:'+h+'px;"></iframe>';
		$('#tabui').tabs('add',{
			title:title,
			content:content,
			closable:true,
			iconCls:icon
		});
	}
}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north'" style="height:82px;">
		<!-- 定义面板 -->
		<div class="easyui-panel" style="height:50px;padding-bottom:0px;background:#B3DFDA;" data-options="border:false">
			<!-- 显示Logo Div -->
			<div style="width:38%;float:left;margin-left:2%">
				<img height="50px" src="/static/images/logo.png"/>
			</div>
			<!-- 显示系统名称 Div -->
			<div style="width:60%;float:right;margin:0;padding:0">
				<p style="font-size:18pt;font-weight:bold;color:black;margin:0;padding:0">古方红糖创客平台</font></p>
			</div>
			<!-- 显示登录用户 Div -->
			<div id="userdiv" style="position:absolute;bottom:30px;right:20px;">
				<font color="black"><b>当前用户:${user.name}</b></font>
			</div>
		</div>
		<!-- 菜单 -->
		<%=menu %>

	</div>
    <div data-options="region:'west',title:'自定义导航'" style="width:150px;heigth:100%;">
		<div class="easyui-panel" style="padding:5px;width:100%;heigth:100%;">
			<ul class="easyui-tree">
				<li>
					<span>My Documents</span>
					<ul>
						<li data-options="state:'closed'">
							<span>Photos</span>
							<ul>
								<li>
									<span>Friend</span>
								</li>
								<li>
									<span>Wife</span>
								</li>
								<li>
									<span>Company</span>
								</li>
							</ul>
						</li>
						<li>
							<span>Program Files</span>
							<ul>
								<li>Intel</li>
								<li>Java</li>
								<li>Microsoft Office</li>
								<li>Games</li>
							</ul>
						</li>
						<li>index.html</li>
						<li>about.html</li>
						<li>welcome.html</li>
					</ul>
				</li>
			</ul>
		</div>
    </div>
    <div data-options="region:'center',title:'办公区域'" style="padding:5px;background:#eee;">

		<div id="tabui" class="easyui-tabs" style="width:100%;height:100%;margin:0px;margin-right:0px;">
		
		</div>

    </div>
</body>
</html>