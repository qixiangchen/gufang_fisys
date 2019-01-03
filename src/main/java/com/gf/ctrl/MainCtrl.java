package com.gf.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;
import com.gf.statusflow.def.TreeNode;

@Controller
public class MainCtrl {
	@Autowired
	private FunctionService serv;
	@Autowired
	private StatusFlowMng sfmng;
	@Autowired
	private IOrgModel orgmodel;
	
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
	public String logindone(HttpServletRequest req,HttpServletResponse resp,
			String loginId,String pwd)
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
	        
			//记录Cookie
			Cookie c = new Cookie("loginId",loginId);  
            c.setPath(req.getContextPath());
            resp.addCookie(c);
			Cookie c2 = new Cookie("pwd",pwd);  
            c2.setPath(req.getContextPath());
            resp.addCookie(c2);
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
	
	@RequestMapping("/function.action")
	public String function()
	{
		return "/orgmodel/function";
	}
	
	
	@ResponseBody
	@RequestMapping("/funcroot.action")
	public List<TreeNode> funcroot()
	{
		FunctionInfo rootFi = orgmodel.getRootFunc();
		if(rootFi == null)
		{
			rootFi = orgmodel.initFunc();
		}
		List<TreeNode> trees = new ArrayList<TreeNode>();
		TreeNode rootTree = new TreeNode();
		rootTree.setId(rootFi.getId().toString());
		rootTree.setText(rootFi.getName());
		rootTree.setState("closed");
		Map attributes = new HashMap();
		attributes.put("parentId",rootFi.getParentId());
		attributes.put("path", "/");
		attributes.put("isload", "false");
		rootTree.setAttributes(attributes);
		
		trees.add(rootTree);
		return trees;
	}
	
	@ResponseBody
	@RequestMapping("/funcsubsave.action")
	public boolean funcsubsave(FunctionInfo fi)
	{
		String parentId = fi.getId();
		if(parentId == null)
		{
			return false;
		}
		fi.setId(UUID.create("function"));
		fi.setParentId(parentId);
		orgmodel.saveFunc(fi);
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/funcsave.action")
	public boolean funcsave(FunctionInfo fi)
	{
		String id = fi.getId();
		if(id == null)
		{
			fi.setId(UUID.create("function"));
			orgmodel.saveFunc(fi);
		}
		else
			orgmodel.updateFunc(fi);
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/funcdel.action")
	public boolean funcdel(FunctionInfo fi)
	{
		orgmodel.deleteFunc(fi.getId());
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/gettreebyid")
	public List<TreeNode> gettreebyid(String id)
	{
		List<FunctionInfo> funs = orgmodel.getChildFunc(id);
		List<TreeNode> rtn = new ArrayList<TreeNode>();
		for(FunctionInfo fi:funs)
		{
			TreeNode ti = new TreeNode();
			ti.setId(fi.getId().toString());
			ti.setText(fi.getName());
			ti.setIconCls(fi.getIcon()!=null?fi.getIcon():"pic_1");
			ti.setUrl(fi.getUrl());
			List<FunctionInfo> chd = orgmodel.getChildFunc(fi.getId());
			if(chd != null && chd.size()>0)
				ti.setState("closed");
			else
				ti.setState("open");
			ti.addAttribute("isload","false");
			if(fi.getPriority() != null)
				ti.addAttribute("priority",fi.getPriority().toString());
			if(fi.getParentId() != null)
				ti.addAttribute("parentId",fi.getParentId().toString());
			rtn.add(ti);
		}
		return rtn;
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
