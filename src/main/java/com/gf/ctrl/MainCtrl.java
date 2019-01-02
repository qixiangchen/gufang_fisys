package com.gf.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/*
		�ҽ�Ѧ����
*/

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.model.FunctionInfo;
import com.gf.service.FunctionService;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.Util;

@Controller
public class MainCtrl {
	@Autowired
	private FunctionService serv;
	@Autowired
	private StatusFlowMng sfmng;
	
	@RequestMapping("/")
	public String login(HttpServletRequest req)
	{
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req)
	{
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "login";
	}
	
	@RequestMapping("/logindone")
	@ResponseBody
	public String logindone(HttpServletRequest req,String loginId,String pwd)
	{
      //添加用户认证信息
    	try
    	{
	        Subject subject = SecurityUtils.getSubject();
	        System.out.println("1=========="+subject.getPrincipal());
	        String pwdMd5 = Util.getMD5(pwd);
	        UsernamePasswordToken upToken = new UsernamePasswordToken(loginId,pwdMd5);
	        //进行验证，这里可以捕获异常，然后返回对应信息
	        subject.login(upToken);
	        System.out.println("2=========="+subject.getPrincipal());
	        return "true";
    	}
    	catch(UnknownAccountException uae)
    	{
    		return "账号错误";
    	}
    	catch(IncorrectCredentialsException ice)
    	{
    		return "密码错误";
    	}

	}
	
	@RequestMapping("/main.action")
	public String main(HttpServletRequest req)
	{
		String userId = "";
		String menu = serv.generateMenuBar(userId);
		req.setAttribute("menu", menu);
		return "main";
	}
	
	@RequestMapping("/expense.action")
	public String expense(HttpServletRequest req)
	{
		StatusMsg wfMsg = null;
		String userId = "1";
		String processId = "testwf";
		String nexttask = null;
		String instWorkitemId = null;
		List<Properties> nextBtnList = null;
		List<Properties> nextActList = null;
		List<Properties> nextUserSelectList = null;
		HashMap hmap = null;
		StatusFlowData wfdata = sfmng.getWorkflowData(userId, processId, nexttask, instWorkitemId,hmap);
		nextBtnList = wfdata.getNextBtnList();
		nextActList = wfdata.getNextActList();
		System.out.println("nextBtnList="+nextBtnList);
		System.out.println("nextActList="+nextActList);
		
		req.setAttribute("button", nextBtnList);
		return "/fi/expense";
	}
}
