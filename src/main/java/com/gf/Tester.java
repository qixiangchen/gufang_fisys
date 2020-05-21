package com.gf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gf.model.FunctionInfo;
import com.gf.service.FunctionService;
import com.gf.statusflow.IOrg;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.def.DefWorkItem;

//测试类
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Starter.class)
public class Tester
{
	@Autowired
	private StatusFlowWAPI wapi;
	@Autowired
	private IOrgModel orgmodel;
	@Autowired
	private StatusFlowMng sfmng;
	
    @Test
    public void testMethod()
    {
    	try
    	{   		
    		/**
    		IOrg org = orgmodel.getOrgById("1");
    		System.out.println("org="+org);
    		
	    	String defProcessId = "testwf";//工作流模板名称
	    	String startUserId = "1";//流程实例启动用户
	    	userId = "1";//操作工作流实例当前用户
	    	String instanceId = "order1";//业务模块ID
	    	String type = "请假单";//流程类型，通常取流程模板名称
	    	String title = "UserA的请假单";//流程实例标题
	    	 nexttask = "account";//下一办理环节
	    	String url = "/holidayapply?id="+instanceId;//业务模块应用链接
	    	hmap = new HashMap();//工作流实例相关数据
	    	hmap.put("days", 5);
	    	String flag = "gf";//区分客户标识，每一个用户分配统一的标识
	    	String testMode = "no";//是否进入测试模式
	    	//wapi.startWorkflow(defProcessId, startUserId, userId, instanceId, type, title, nexttask, url, hmap, flag, testMode);
	    	nexttask = "draft";
	    	String instProcessId = "proc5bfe5bb88e25";//流程实例ID
	    	String instActivityId = "acti5bfea9d88e25";//流程Activity实例ID
	    	instWorkitemId=  "work5bfea9d98e25";//待办件实例ID
	    	List<String> userIdLst = new ArrayList<String>();
	    	userIdLst.add("2");//下一办理人
	    	//wapi.submitWorkflow(instProcessId, instActivityId, instWorkitemId, userIdLst, instanceId, type, title, nexttask, url, hmap, flag, testMode);
	    	
	    	//获取某用户办结件列表
	    	List lst = wapi.getWorkitemList("2", testMode);
	    	System.out.println("list="+lst);
	    	for(Iterator it=lst.iterator();it.hasNext();)
	    	{
	    		DefWorkItem dwi = (DefWorkItem)it.next();
	    		System.out.println("instProcessId="+dwi.getInstProcessId());
	    		System.out.println("instActivityId="+dwi.getInstActivityId());
	    		System.out.println("instWorkitem="+dwi.getId());
	    		System.out.println("title="+dwi.getTitle());
	    		System.out.println("url="+dwi.getUrl());
	    	}
	    	*/
	    }
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
