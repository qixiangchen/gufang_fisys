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
function loaddatagrid(orgId)
{
	$('#userdg').datagrid({
		url:'/userload?orgId='+orgId
	})
}
function chkpwd()
{
	var pwd = $('#password').val();
	var repwd = $('#repwd').val();
	if(pwd != repwd)
	{
		$.messager.alert('信息','两次密码不一致');
	}
}
function adduser()
{
	$('#win').window('open');
	$('#frm').form('clear');
	$('#enabled').combobox('setValue','0');
	$('#locked').combobox('setValue','no');
}
function closeuser()
{
	$('#win').window('close');
}
function saveuser()
{
	var rtn = $('#frm').form('validate');
	if(rtn)
	{
		orgId = $('#orgId').combobox('getValue');
		$('#frm').form('submit', {    
		    url:'/usersave',    
		    onSubmit: function(){    
	  
		    },    
		    success:function(data){
		    	loaddatagrid(orgId);
		    	closeuser();
		    }
		});
	}
}
function openuser(index,rowObj)
{
	$('#win').window('open');
	$('#id').val(rowObj.id);
	$('#orgPath').val(rowObj.orgPath);
	$('#openid').val(rowObj.openid);
	$('#flag').val(rowObj.flag);
	$('#testMode').val(rowObj.testMode);
	$('#loginId').textbox('setValue',rowObj.loginId);
	$('#name').textbox('setValue',rowObj.name);
	//$('#password').passwordbox('setValue',rowObj.password);
	$('#companyMail').textbox('setValue',rowObj.companyMail);
	$('#privateMail').textbox('setValue',rowObj.privateMail);
	$('#address').textbox('setValue',rowObj.address);
	$('#companyTeleNo').textbox('setValue',rowObj.companyTeleNo);
	$('#mobile').textbox('setValue',rowObj.mobile);
	$('#title').textbox('setValue',rowObj.title);
	$('#desc').textbox('setValue',rowObj.desc);
	$('#enabled').combobox('setValue',rowObj.enabled);
	$('#managerId').textbox('setValue',rowObj.managerId);
	$('#birthday').datebox('setValue',rowObj.birthday);
	$('#cardId').textbox('setValue',rowObj.cardId);
	$('#seqno').textbox('setValue',rowObj.seqno);
	$('#failureDate').datebox('setValue',rowObj.failureDate);
	$('#cardId').textbox('setValue',rowObj.cardId);
	$('#orgId').combobox('setValue',rowObj.orgId);
}
$(document).ready(
		function()
		{
			reloadtree();
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
					var orgId = node.id;
					loaddatagrid(orgId);	
				}
			});
			
			$('#repwd').textbox({
				'onChange':chkpwd
			});
			$('#win').window('close');
			$('#userdg').datagrid({'onClickRow':openuser})
		}
	);		
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'自定义导航'" style="width:250px;">
		<ul id="tree" class="easyui-tree" data-options="lines:true,checkbox:true,cascadeCheck:false">   
		</ul> 
    </div>
    <div data-options="region:'center',title:'用户维护'" style="padding:5px;">
		<table id="userdg" class="easyui-datagrid" style="width:100%;height:250px"   
		        data-options="url:'/userload',fitColumns:true,pagination:true,
		        pageSize:10,pageList:[10,50,100,200],toolbar:'#tb'">   
		    <thead>   
		        <tr>
		        	<th data-options="field:'id',width:100,hidden:true">ID</th>   
		            <th data-options="field:'loginId',width:100">登录ID</th>   
		            <th data-options="field:'name',width:100">姓名</th>   
		            <th data-options="field:'address',width:100">地址</th>
		            <th data-options="field:'companyMail',width:100">公司邮箱</th>  
		            <th data-options="field:'privateMail',width:100">私人邮箱</th>  
		            <th data-options="field:'companyTeleNo',width:100">公司电话</th>
		            <th data-options="field:'homeTeleNo',width:100">家庭电话</th>
		            <th data-options="field:'mobile',width:100">手机</th>
		            <th data-options="field:'title',width:100">职位</th>
		            <th data-options="field:'desc',width:100,hidden:true">ID</th>
		            <th data-options="field:'orgId',width:100,hidden:true">ID</th>
		            <th data-options="field:'orgPath',width:100,hidden:true">ID</th>
		            <th data-options="field:'enabled',width:100,hidden:true">ID</th>
		            <th data-options="field:'locked',width:100,hidden:true">ID</th>
		            <th data-options="field:'managerId',width:100,hidden:true">ID</th>
		            <th data-options="field:'birthday',width:100,hidden:true">ID</th>
		            <th data-options="field:'cardId',width:100,hidden:true">ID</th>
		            <th data-options="field:'failureDate',width:100,hidden:true">ID</th>
		            <th data-options="field:'failureCount',width:100,hidden:true">ID</th>
		            <th data-options="field:'openid',width:100,hidden:true">ID</th>
		            <th data-options="field:'seqno',width:100,hidden:true">ID</th>
		            <th data-options="field:'flag',width:100,hidden:true">ID</th>
		            <th data-options="field:'testMode',width:100,hidden:true">ID</th>
		        </tr>   
		    </thead>   
		</table>
		<div id="tb">
			<a id="btn" onclick="adduser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">添加用户</a>
			<a id="btn" onclick="deleteuser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">删除用户</a>
		</div>

		<div id="win" class="easyui-window" title="用户窗口" style="width:600px;height:480px"   
		        data-options="iconCls:'icon-save',modal:true">   
		    <div class="easyui-layout" data-options="fit:true">      
		        <div data-options="region:'center'">   
					<form id="frm" method="post">
						<input type="hidden" id="id" name="id"/>
						<input type="hidden" id="orgPath" name="orgPath"/>
						<input type="hidden" id="openid" name="openid"/>
						<input type="hidden" id="flag" name="flag"/>
						<input type="hidden" id="testMode" name="testMode"/>
					    <div style="margin-left:50px;margin-top:10px">   
					        <input class="easyui-textbox" type="text" id="loginId" name="loginId" data-options="width:250,label:'登录名:',required:true" />   
					        <input class="easyui-textbox" type="text" id="name" name="name" data-options="width:250,label:'姓名:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px"> 
					        <input class="easyui-passwordbox" type="text" id="password" name="password" data-options="width:250,label:'密码:'" />   
					        <input class="easyui-passwordbox" type="text" id="repwd" name="repwd" onclick="chkpwd()" data-options="width:250,label:'密码:'" />
					    </div>
					    <div style="margin-left:50px;margin-top:10px"> 
					        <input class="easyui-textbox" type="text" id="companyMail" name="companyMail" data-options="width:250,label:'公司邮箱:',required:true,validType:'email'" />   
					        <input class="easyui-textbox" type="text" id="privateMail" name="privateMail" data-options="width:250,label:'私人邮箱:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">
					    	<input class="easyui-textbox" type="text" id="address" name="address" data-options="width:250,label:'地址:'" />  
					        <input class="easyui-textbox" type="text" id="companyTeleNo" name="companyTeleNo" data-options="width:250,label:'公司电话:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">
					    	<input class="easyui-textbox" type="text" id="mobile" name="mobile" data-options="width:250,label:'手机号:'" />  
					        <input class="easyui-textbox" type="text" id="title" name="title" data-options="width:250,label:'职位:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">
					    	<input class="easyui-textbox" type="text" id="desc" name="desc" data-options="width:250,label:'描述:'" />    
							<select id="enabled" name="enabled" class="easyui-combobox" data-options="width:250,label:'是否有效:'" >   
							    <option value="0">有效</option>   
							    <option value="1">无效</option>     
							</select> 
					    </div>
					    <div style="margin-left:50px;margin-top:10px">
							<select id="locked" name="locked" class="easyui-combobox" data-options="width:250,label:'是否锁定:'" >   
							    <option value="no">正常</option>   
							    <option value="yes">已锁</option>     
							</select>   
					        <input class="easyui-textbox" type="text" id="managerId" name="managerId" data-options="width:250,label:'上司:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">
 					    	<input class="easyui-datebox" type="text" id="birthday" name="birthday" data-options="width:250,label:'生日:'" />  
					        <input class="easyui-textbox" type="text" id="cardId" name="cardId" data-options="width:250,label:'身份证:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">
 					    	<input class="easyui-textbox" type="text" id="seqno" name="seqno" data-options="width:250,label:'序号:'" />  
					        <input class="easyui-datebox" type="text" id="failureDate" name="failureDate" data-options="width:250,label:'登录失败时间:'" />   
					    </div>
					    <div style="margin-left:50px;margin-top:10px">   
							<select id="orgId" name="orgId" class="easyui-combobox" 
								data-options="width:250,label:'所在部门:',valueField:'id',textField:'name',url:'/orgload'" >     
							</select>  
					    </div>
			  		    <div style="margin-left:50px;margin-top:30px"> 
							<a id="btn" onclick="saveuser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">保存</a>  
							<a id="btn" onclick="closeuser()" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">关闭</a>    
					    </div>
					</form>
		        </div>   
		    </div>   
		</div>  
    </div>
</body>
</html>