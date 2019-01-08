package com.gf.statusflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gf.statusflow.def.DefProcess;
import com.gf.statusflow.def.DefWorkItem;

@Component
public class StatusFlowMng{
	private Logger log = LoggerFactory.getLogger(StatusFlowMng.class);
	@Autowired
	private WorkflowMapper mapper;
	@Autowired
	private StatusFlowWAPI wapi;
	
	public StatusFlowWAPI getWapi() {
		return wapi;
	}

	public void setWapi(StatusFlowWAPI wapi) {
		this.wapi = wapi;
	}
	
	public String getUrl(String nexttask,String defProcessId) throws Exception
	{
		return wapi.getUrl(nexttask, defProcessId); 
	}
	
	public StatusFlowData getWorkflowData(String userId,String processId,String instWorkitemId)
	{
		String nexttask = null;
		StatusFlowData wfdata = new StatusFlowData();
		StatusMsg wfMsg = new StatusMsg();
		List<Properties> nextBtnList = null;
		List<Properties> nextActList = null;
		List<Properties> nextUserSelectList = null;
		
		if(nextBtnList == null)
			nextBtnList = new ArrayList<Properties>();
		if(nextActList == null)
			nextActList = new ArrayList<Properties>();
		if(nextUserSelectList == null)
			nextUserSelectList = new ArrayList<Properties>();
		HashMap hmap = new HashMap();
		wfdata.setNextActList(nextActList);
		wfdata.setNextBtnList(nextBtnList);
		wfdata.setNextUserSelectList(nextUserSelectList);
		wfdata.setWfMsg(wfMsg);
		log.debug("getWorkflowData userId="+userId+",processId="+processId);
		log.debug("getWorkflowData nexttask="+nexttask+",instWorkitemId="+instWorkitemId);
		log.debug("getWorkflowData nextBtnList="+nextBtnList+",nextActList="+nextActList);
		log.debug("getWorkflowData nextUserSelectList="+nextUserSelectList+",hmap="+hmap);
		try
		{
			processId = Util.getLastWorkflow(processId);
			//获取工作流下一办理节点属性列表
			if("".equals(Util.fmtStr(instWorkitemId)))
				nextActList = wapi.getSecondTask(nexttask,userId,processId,wfMsg);
			else
				nextActList = wapi.getNextTask(nexttask,instWorkitemId,wfMsg);
			for(Object obj:nextActList)
			{
				//根据表单域决定节点办理人列表
				if(obj instanceof List)
				{
					nextUserSelectList = (List<Properties>)obj;
				}
			}
			if(!wfMsg.isOk())
				return wfdata;
			if(nextActList == null)
			{
				log.error("WF nextActivityList is null,processId="+processId+",instWorkitemId="+instWorkitemId);
			}
			nextBtnList.clear();
			boolean isConditioAct = false;
			String cdnName = null;
			for(Object obj:nextActList)
			{
				if(obj instanceof Properties)
				{
					Properties prop = (Properties)obj;
					List tempUserIdLst = new ArrayList();
					String id = prop.getProperty("id");
					String actor = prop.getProperty("actor");
					String excludeRole = prop.getProperty("excluderole");
					String condition = prop.getProperty("condition");
					String type = prop.getProperty("type");
					//如果是条件分支节点,忽略工作流提交按钮
					if("switch".equals(Util.fmtStr(type)))
					{
						isConditioAct = true;
						cdnName = prop.getProperty("name");
						continue;
					}
					if(actor.endsWith(";"))
						actor = actor.substring(0,actor.length()-1);
					//参与者选择列表
					String actorsel = prop.getProperty("actorsel");
					if(actorsel.endsWith(";"))
						actorsel = actorsel.substring(0,actorsel.length()-1);
					String name = prop.getProperty("name");
					Properties prop2 = new Properties();
					prop2.setProperty("id",id);
					prop2.setProperty("name",name);
					prop2.setProperty("actor",actor);
					prop2.setProperty("js","wfsubmit('"+id+"','"+actor+"','"+actorsel+"')");
					//添加工作流提交按钮列表
					nextBtnList.add(prop2);
					//解析对应节点办理人列表
					StringTokenizer st = new StringTokenizer(actor,";");			
					for(;st.hasMoreElements();)
					{
						String idName = st.nextToken();
						int pos = idName.indexOf(",");
						if(pos > 0)
						{
							String userId2 = idName.substring(0,pos);
							tempUserIdLst.add(userId2);
						}
					}
					hmap.put(id, tempUserIdLst);
				}
			}
			//添加条件分支工作节点的虚拟节点
			if(isConditioAct)
			{
				Properties prop2 = new Properties();
				prop2.setProperty("id","conditionActivity");
				prop2.setProperty("name",cdnName);
				prop2.setProperty("actor","id,name");
				prop2.setProperty("js","wfsubmit('conditionActivity','id,name','')");
				nextBtnList.add(prop2);			
			}
			log.debug("nextActList="+nextActList);
			log.debug("nextBtnList="+nextBtnList);
			log.debug("userId = "+userId);
			log.debug("hmap = "+hmap);
		}
		catch(Exception e)
		{
			log.error("getWorkflowData", e);
		}
		return wfdata;
	}
	
	public StatusFlowData getWorkflowData(String userId,String processId,
			String nexttask,String instWorkitemId,HashMap hmap)
	{
		StatusFlowData wfdata = new StatusFlowData();
		StatusMsg wfMsg = new StatusMsg();
		List<Properties> nextBtnList = null;
		List<Properties> nextActList = null;
		List<Properties> nextUserSelectList = null;
		
		if(nextBtnList == null)
			nextBtnList = new ArrayList<Properties>();
		if(nextActList == null)
			nextActList = new ArrayList<Properties>();
		if(nextUserSelectList == null)
			nextUserSelectList = new ArrayList<Properties>();
		if(hmap == null)
			hmap = new HashMap();
		wfdata.setNextActList(nextActList);
		wfdata.setNextBtnList(nextBtnList);
		wfdata.setNextUserSelectList(nextUserSelectList);
		wfdata.setWfMsg(wfMsg);
		log.debug("getWorkflowData userId="+userId+",processId="+processId);
		log.debug("getWorkflowData nexttask="+nexttask+",instWorkitemId="+instWorkitemId);
		log.debug("getWorkflowData nextBtnList="+nextBtnList+",nextActList="+nextActList);
		log.debug("getWorkflowData nextUserSelectList="+nextUserSelectList+",hmap="+hmap);
		try
		{
			processId = Util.getLastWorkflow(processId);
			//获取工作流下一办理节点属性列表
			if("".equals(Util.fmtStr(instWorkitemId)))
				nextActList = wapi.getSecondTask(nexttask,userId,processId,wfMsg);
			else
				nextActList = wapi.getNextTask(nexttask,instWorkitemId,wfMsg);
			for(Object obj:nextActList)
			{
				//根据表单域决定节点办理人列表
				if(obj instanceof List)
				{
					nextUserSelectList = (List<Properties>)obj;
				}
			}
			if(!wfMsg.isOk())
				return wfdata;
			if(nextActList == null)
			{
				log.error("WF nextActivityList is null,processId="+processId+",instWorkitemId="+instWorkitemId);
			}
			nextBtnList.clear();
			boolean isConditioAct = false;
			String cdnName = null;
			for(Object obj:nextActList)
			{
				if(obj instanceof Properties)
				{
					Properties prop = (Properties)obj;
					List tempUserIdLst = new ArrayList();
					String id = prop.getProperty("id");
					String actor = prop.getProperty("actor");
					String excludeRole = prop.getProperty("excluderole");
					String condition = prop.getProperty("condition");
					String type = prop.getProperty("type");
					//如果是条件分支节点,忽略工作流提交按钮
					if("switch".equals(Util.fmtStr(type)))
					{
						isConditioAct = true;
						cdnName = prop.getProperty("name");
						continue;
					}
					if(actor.endsWith(";"))
						actor = actor.substring(0,actor.length()-1);
					//参与者选择列表
					String actorsel = prop.getProperty("actorsel");
					if(actorsel.endsWith(";"))
						actorsel = actorsel.substring(0,actorsel.length()-1);
					String name = prop.getProperty("name");
					Properties prop2 = new Properties();
					prop2.setProperty("id",id);
					prop2.setProperty("name",name);
					prop2.setProperty("actor",actor);
					prop2.setProperty("js","wfsubmit('"+id+"','"+actor+"','"+actorsel+"')");
					//添加工作流提交按钮列表
					nextBtnList.add(prop2);
					//解析对应节点办理人列表
					StringTokenizer st = new StringTokenizer(actor,";");			
					for(;st.hasMoreElements();)
					{
						String idName = st.nextToken();
						int pos = idName.indexOf(",");
						if(pos > 0)
						{
							String userId2 = idName.substring(0,pos);
							tempUserIdLst.add(userId2);
						}
					}
					hmap.put(id, tempUserIdLst);
				}
			}
			//添加条件分支工作节点的虚拟节点
			if(isConditioAct)
			{
				Properties prop2 = new Properties();
				prop2.setProperty("id","conditionActivity");
				prop2.setProperty("name",cdnName);
				prop2.setProperty("actor","id,name");
				prop2.setProperty("js","wfsubmit('conditionActivity','id,name','')");
				nextBtnList.add(prop2);			
			}
			log.debug("nextActList="+nextActList);
			log.debug("nextBtnList="+nextBtnList);
			log.debug("userId = "+userId);
			log.debug("hmap = "+hmap);
		}
		catch(Exception e)
		{
			log.error("getWorkflowData", e);
		}
		return wfdata;
	}

	public DefWorkItem saveStartActivity(String processId,String userId,String title,String url,
			String instanceId,java.util.HashMap hmap,String flag2,String testMode) throws Exception
	{
		DefWorkItem oldDwi = wapi.getStartWorkItem(instanceId);
		if(oldDwi != null)
		{
			oldDwi.setTitle(title);
			mapper.updateWorkitem(oldDwi);
			return oldDwi;
		}
		StatusMsg wfMsg = new StatusMsg();
		DefProcess defProc = wapi.getProcess(processId,wfMsg);
		DefWorkItem dwi = wapi.saveStartWorkItem(defProc.getId(),defProc.getName(),userId,title,url,instanceId,hmap,flag2,testMode);
		return dwi;
	}
	
	public void finishStartWorkItem(String instanceId) throws Exception
	{
		wapi.finishStartWorkItem(instanceId);
	}
	
	public boolean isTrueCondition(HashMap hmap,String condition) throws Exception
	{
		System.out.println("hmap="+hmap+",condition="+condition);
		//String key = (String)hmap.get("key");
		String key = condition.substring(0,condition.indexOf(" "));
		Object switchVar = hmap.get(key);	
		if(switchVar instanceof Float)
		{
			Float amount = (Float)switchVar;
			int pos = condition.lastIndexOf(" ");
			if(pos == -1)
				return false;
			String val = condition.substring(pos+1,condition.length());
			val = val.trim();
			Float f = new Float(val);
			if(condition.indexOf("EQ")>0)
			{
				if(amount==f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("GT")>0)
			{
				if(amount>f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("GE")>0)
			{
				if(amount>=f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("LT")>0)
			{
				if(amount<f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("LE")>0)
			{
				if(amount<=f)
					return true;
				else
					return false;
			}
		}
		if(switchVar instanceof BigDecimal)
		{
			BigDecimal amount = (BigDecimal)switchVar;
			int pos = condition.lastIndexOf(" ");
			if(pos == -1)
				return false;
			String val = condition.substring(pos+1,condition.length());
			val = val.trim();
			Float f = new Float(val);
			if(condition.indexOf("EQ")>0)
			{
				if(amount.floatValue()==f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("GT")>0)
			{
				if(amount.floatValue()>f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("GE")>0)
			{
				if(amount.floatValue()>=f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("LT")>0)
			{
				if(amount.floatValue()<f)
					return true;
				else
					return false;
			}
			if(condition.indexOf("LE")>0)
			{
				if(amount.floatValue()<=f)
					return true;
				else
					return false;
			}
		}
		if(switchVar instanceof String)
		{
			String amount = (String)switchVar;
			int pos = condition.lastIndexOf(" ");
			if(pos == -1)
				return false;
			String val = condition.substring(pos+1,condition.length());
			val = val.trim();
			if(val.startsWith("'"))
				val = val.substring(1,val.length());
			if(val.endsWith("'"))
				val = val.substring(0,val.length()-1);
			if(condition.indexOf("!EQ")>0)
			{
				if(amount.equals(val))
					return false;
				else
					return true;
			}
			else
			{
				if(amount.equals(val))
					return true;
				else
					return false;
			}
		}
		return false;
	}
}
