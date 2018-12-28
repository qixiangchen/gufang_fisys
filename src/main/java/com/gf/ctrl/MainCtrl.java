package com.gf.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/*
		Œ“Ω–—¶—Ù—Ù
*/

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gf.model.FunctionInfo;
import com.gf.service.FunctionService;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusMsg;

@Controller
public class MainCtrl {
	@Autowired
	private FunctionService serv;
	@Autowired
	private StatusFlowMng sfmng;
	
	@RequestMapping("/main")
	public String main(HttpServletRequest req)
	{
		String userId = "";
		String menu = serv.generateMenuBar(userId);
		req.setAttribute("menu", menu);
		return "main";
	}
	
	@RequestMapping("/expense")
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
