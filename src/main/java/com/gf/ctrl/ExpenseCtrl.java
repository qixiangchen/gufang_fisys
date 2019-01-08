package com.gf.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.model.ExpenseInfo;
import com.gf.service.ExpenseService;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.IUser;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;

@Controller
public class ExpenseCtrl {
	@Autowired
	private StatusFlowMng sfmng;
	@Autowired
	private StatusFlowWAPI wapi;
	@Autowired
	private IOrgModel orgmodel;
	@Autowired
	private ExpenseService expserv;

	@RequestMapping("/expense.action")
	public String expense(HttpServletRequest req)
	{
		String instanceid = req.getParameter("instanceid");
		String instprocessid = req.getParameter("instprocessid");
		String instactivityid = req.getParameter("instactivityid");
		String instworkitemid = req.getParameter("instworkitemid");

		StatusMsg wfMsg = null;
		String nexttask = null;
		List<Properties> nextBtnList = null;
		List<Properties> nextActList = null;
		IUser loginUser = Util.getLoginUser();
		String userId = loginUser.getId();
		String processId = "expense";
		HashMap hmap = null;
		StatusFlowData wfdata = sfmng.getWorkflowData(userId, processId, nexttask, instworkitemid,hmap);
		nextBtnList = wfdata.getNextBtnList();
		nextActList = wfdata.getNextActList();
		req.setAttribute("button", nextBtnList);
		if(instanceid != null)
		{
			ExpenseInfo eps = expserv.selectById(instanceid);
			req.setAttribute("eps", eps);
			req.setAttribute("instprocessid", instprocessid);
			req.setAttribute("instactivityid", instactivityid);
			req.setAttribute("instworkitemid", instworkitemid);
		}
		System.out.println("nextActList==="+nextActList);
		return "/fi/expense";
	}
	
	@RequestMapping("/expsubmit.action")
	@ResponseBody
	public StatusMsg expsave(HttpServletRequest req,ExpenseInfo exps)
	{
		try
		{
			String instanceid = exps.getId();
			if("".equals(Util.fmtStr(instanceid)))
				instanceid = UUID.create("expense");
			String instprocessid = req.getParameter("instprocessid");
			String instactivityid = req.getParameter("instactivityid");
			String instworkitemid = req.getParameter("instworkitemid");
			String nexttask = req.getParameter("nexttask");
			String nextUserId = req.getParameter("nextUserId");
			
			IUser loginUser = Util.getLoginUser();
			String startUserId = loginUser.getId();
			String processId = "expense";
			String type = "expense";
			String title = exps.getName()+":"+exps.getAmount().toString();
			String url = "/expense.action";
			HashMap hmap = new HashMap();
			String flag = "gf";
			String testMode = "no";
			exps.setId(instanceid);
			StatusMsg wfMsg = null;
			if("".equals(Util.fmtStr(instworkitemid)))
			{
				expserv.insert(exps);
				List<String> userIdList = new ArrayList<String>();
				String[] dim = nextUserId.split(",");
				for(String s:dim)
				{
					if(!"".equals(Util.fmtStr(s)))
						userIdList.add(s);
				}
				wfMsg = wapi.startWorkflow(processId,startUserId,userIdList,instanceid,type,
						title,nexttask,url,hmap,flag,testMode);
			}
			else
			{
				List<String> userIdList = new ArrayList<String>();
				String[] dim = nextUserId.split(",");
				for(String s:dim)
				{
					if(!"".equals(Util.fmtStr(s)))
						userIdList.add(s);
				}
					
				expserv.updateById(exps);
				wfMsg = wapi.submitWorkflow(instprocessid,instactivityid,instworkitemid,userIdList,instanceid,
						type,title,nexttask,url,hmap,flag,testMode);
			}
			return wfMsg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
