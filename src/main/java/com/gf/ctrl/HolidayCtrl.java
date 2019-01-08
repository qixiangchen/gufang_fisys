package com.gf.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.model.ExpenseInfo;
import com.gf.model.HolidayInfo;
import com.gf.service.ExpenseService;
import com.gf.service.HolidayService;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.IUser;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;

@Controller
public class HolidayCtrl {
	private Logger log = LoggerFactory.getLogger(HolidayCtrl.class);
	
	@Autowired
	private StatusFlowMng sfmng;
	@Autowired
	private StatusFlowWAPI wapi;
	@Autowired
	private IOrgModel orgmodel;
	@Autowired
	private HolidayService serv;

	@RequestMapping("/holiday.action")
	public String holiday(HttpServletRequest req)
	{
		String instanceid = req.getParameter("instanceid");//业务实例ID
		String instprocessid = req.getParameter("instprocessid");//工作流实例ID
		String instactivityid = req.getParameter("instactivityid");//工作流活动ID
		String instworkitemid = req.getParameter("instworkitemid");//工作流代办工作ID
		StatusMsg wfMsg = null;
		String nexttask = null;
		List<Properties> nextBtnList = null;
		List<Properties> nextActList = null;
		IUser loginUser = Util.getLoginUser();
		String userId = loginUser.getId();
		String processId = "holiday";
		HashMap hmap = null;
		StatusFlowData wfdata = sfmng.getWorkflowData(userId, processId, nexttask, instworkitemid,hmap);
		nextBtnList = wfdata.getNextBtnList();
		nextActList = wfdata.getNextActList();
		req.setAttribute("button", nextBtnList);
		System.out.println("nextBtnList==="+nextBtnList);
		if(instworkitemid != null)//新建请假单，工作流数据库为空
		{
			HolidayInfo hdi = serv.selectById(instanceid);
			req.setAttribute("hdi", hdi);
		}
		else
		{
			
		}
		req.setAttribute("instprocessid", instprocessid);
		req.setAttribute("instactivityid", instactivityid);
		req.setAttribute("instworkitemid", instworkitemid);
		System.out.println("nextActList==="+nextActList);
		return "/holiday/holiday";
	}
	
	@RequestMapping("/holidaysubmit.action")
	@ResponseBody
	public StatusMsg holidaysubmit(HttpServletRequest req,HolidayInfo hdi)
	{
		try
		{
			String instprocessid = req.getParameter("instprocessid");
			String instactivityid = req.getParameter("instactivityid");
			String instworkitemid = req.getParameter("instworkitemid");
			String defProcessId = "holiday";
			IUser loginUser = Util.getLoginUser();
			String startUserId = loginUser.getId();
			String nextUserId = req.getParameter("nextUserId");
			String nexttask = req.getParameter("nexttask");
			log.debug("holidaysubmit instprocessid="+instprocessid);
			log.debug("holidaysubmit instactivityid="+instactivityid);
			log.debug("holidaysubmit instworkitemid="+instworkitemid);
			log.debug("holidaysubmit nextUserId="+nextUserId);
			log.debug("holidaysubmit nexttask="+nexttask);
    		String type = "请假流程";
    		String title = "请假单,"+loginUser.getName()+"请假"+hdi.getDays()+"天";
    		String url = "/holiday.action";
    		HashMap hmap = new HashMap();
    		hmap.put("days", hdi.getDays());
    		String flag2 = "gf";
    		String testMode = "no";
			//创建流程
			if("".equals(Util.fmtStr(instworkitemid)))
			{
				log.debug("holidaysubmit startWorkflow");
				String instanceId = UUID.create("holiday");
				hdi.setId(instanceId);
				serv.insert(hdi);
				List<String> userIdList = new ArrayList<String>();
				String[] dim = nextUserId.split(",");
				for(String s:dim)
				{
					if(!"".equals(Util.fmtStr(s)))
						userIdList.add(s);
				}
	    		StatusMsg msg = wapi.startWorkflow(defProcessId, startUserId, 
	    				userIdList, instanceId, type, title, 
	    				nexttask, url, hmap, flag2, testMode);
	    		return msg;
			}
			else//提交流程
			{
				log.debug("holidaysubmit submitWorkflow");
				serv.updateById(hdi);
				String instanceId = hdi.getId();
				List<String> userIdList = new ArrayList<String>();
				String[] dim = nextUserId.split(",");
				for(String s:dim)
				{
					if(!"".equals(Util.fmtStr(s)))
						userIdList.add(s);
				}
	    		wapi.submitWorkflow(instprocessid, instactivityid, instworkitemid, 
	    				userIdList, instanceId, type, title, nexttask, url, 
	    				hmap, flag2, testMode);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new StatusMsg();
	}

}
