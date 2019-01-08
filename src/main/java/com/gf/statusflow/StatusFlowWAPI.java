package com.gf.statusflow;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.gf.statusflow.def.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
* StatusFlow基于状态控制简易流程管理,流程接口引入Hibernate2及Hibernate3的Session参数
* bInTrans=true,流程内部不对数据库事务做控制，由外层应用统一控制事务
* bInTrans=false,流程控制数据库事务
* Hibernate2 Session和Hibernate3 Session其一有效
*
*/

@Component
public class StatusFlowWAPI
{
	private Log log = LogFactory.getLog(StatusFlowWAPI.class);
	@Autowired
	private WorkflowMapper mapper;
	@Autowired
	private DependenceUtil dpdutil;
	@Autowired
	private UiImage uiimg = null;
	
	public StatusFlowWAPI()
	{

	}

	/**
	* 根据流程定义ID获取流程对象DefProcess
	*/
	public DefProcess getProcess(String defProcessId,StatusMsg msg) throws Exception
	{
		DefProcess defProcess = null;
		if(msg == null)
			msg = new StatusMsg();
		try
		{
			//工作流模板存放在${GUFANG_HOME}/workflow
			String home = dpdutil.getHome();
			String path = home + "/workflow/";
			String filename = path + defProcessId + ".xml";			
			java.io.FileInputStream fis = new java.io.FileInputStream(filename);
			DefParse defParse = new DefParse(fis,defProcessId);
			defProcess = defParse.getDefProcess();
			fis.close();
			msg.setOk(true);
			msg.setCode("WF000000");
			msg.setMsg("ok");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			msg.setOk(false);
			msg.setCode("WF000010");
			msg.setMsg("WF000010 工作流模板 "+defProcessId+" 未定义,请检查com/gufang/statusflow/或者GUFANG_HOME目录是否存在模板文件");
			throw new Exception("WF000010 工作流模板 "+defProcessId+" 未定义,请检查com/gufang/statusflow/或者GUFANG_HOME目录是否存在模板文件");
		}
		return defProcess;
	}

	//检查工作流参数是否合法
	private DefProcess checkValid(String defProcessId,String instanceId,String type,String title,
		String url,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		if(instanceId == null || "".equals(instanceId))
		{
			msg.setOk(false);
			msg.setCode("WF000013");
			msg.setMsg("WF000013 业务数据ID(instanceid) "+instanceId+" 未定义");
			throw new Exception("WF000013 业务数据ID(instanceid) "+instanceId+" 未定义");
		}
		if(type == null || "".equals(type))
		{
			msg.setOk(false);
			msg.setCode("WF000011");
			msg.setMsg("WF000011 业务分类(type) "+type+" 未定义");

			throw new Exception("WF000003 业务分类(type) "+type+" 未定义");
		}
		
		DefProcess defProcess = getProcess(defProcessId,msg);
		return defProcess;
	}
	
	//通过工作流定义取得某节点表单域属性定义列表
	public java.util.List<DefField> getField(String nexttask,String defProcessId) throws Exception
	{
		StatusMsg msg = new StatusMsg();
		java.util.List<DefField> rtn = new java.util.ArrayList<DefField>();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return rtn;
		}
		java.util.List lst = defProcess.getStatusLst();
		for(Iterator it=lst.iterator();it.hasNext();)
		{
			DefStatus ds = (DefStatus)it.next();
			if(ds.getId().equals(nexttask))
				return ds.getFieldLst();
		}
		return rtn;
	}
	
	//通过工作流定义取得某节点表单域属性定义列表
	public java.util.List<DefButton> getButton(String nexttask,String defProcessId) throws Exception
	{
		StatusMsg msg = new StatusMsg();
		java.util.List<DefButton> rtn = new java.util.ArrayList<DefButton>();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return rtn;
		}
		java.util.List lst = defProcess.getStatusLst();
		for(Iterator it=lst.iterator();it.hasNext();)
		{
			DefStatus ds = (DefStatus)it.next();
			if(ds.getId().equals(nexttask))
				return ds.getButtonLst();
		}
		return rtn;
	}
	
	//通过工作流定义取得某节点表单域属性定义列表
	public String getUrl(String nexttask,String defProcessId) throws Exception
	{
		StatusMsg msg = new StatusMsg();
		java.util.List<DefField> rtn = new java.util.ArrayList<DefField>();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return null;
		}
		java.util.List lst = defProcess.getStatusLst();
		for(Iterator it=lst.iterator();it.hasNext();)
		{
			DefStatus ds = (DefStatus)it.next();
			if(ds.getId().equals(nexttask))
				return ds.getUrl();
		}
		return null;
	}
	
	//通过工作流定义取得某节点表单域属性定义列表
	public String getUrlByInstWorkitemId(String nexttask,String instWorkitemId) throws Exception
	{
		DefWorkItem dwi = this.getWorkItem(instWorkitemId);
		return getUrl(nexttask,dwi.getProcessId());
	}
	
	//通过工作流定义取得流程图文件
	public String getUiwfByProcessId(String defProcessId) throws Exception
	{
		StatusMsg msg = new StatusMsg();
		java.util.List<DefField> rtn = new java.util.ArrayList<DefField>();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return null;
		}
		return defProcess.getUiwf();
	}
	
	//通过工作流定义取得流程图文件
	public String getUiwfByInstWorkitemId(String instWorkitemId) throws Exception
	{
		DefWorkItem dwi = getWorkItem(instWorkitemId);
		if(dwi == null)
			return null;
		return getUiwfByProcessId(dwi.getProcessId());
	}

	//通过工作流定义取得第三流程节点名称及参数，第一个节点是开始节点
	public java.util.List getSecondTask(String nexttask,String invokeUserId,String defProcessId,
			StatusMsg msg)throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		java.util.List rtn = new java.util.ArrayList();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return rtn;
		}
		//条件选择参与者
		List<Properties> selUserList = new ArrayList<Properties>();
		DefStatus defStartStatus = defProcess.getStart();
		java.util.List tolst = defStartStatus.getToStatusLst();
		for(java.util.Iterator it=tolst.iterator();it.hasNext();)
		{
			DefToStatus toStatus = (DefToStatus)it.next();
			java.util.Properties prop = new java.util.Properties();
			prop.setProperty("id",toStatus.getId());
			prop.setProperty("name",toStatus.getName());
			prop.setProperty("type",Util.fmtStr(toStatus.getType()));
			String actorstr = "";
			String actorsel = "";
			if("false".equals(toStatus.getCondition()))
			{
				String nextId = toStatus.getId();
				DefStatus nextStatus = defProcess.getStatus(nextId);
				prop.setProperty("excluderole", dpdutil.fmtStr(nextStatus.getExcludeRole()));
				prop.setProperty("condition",Util.fmtStr(nextStatus.getCondition()));
				//取得下一环节参与者列表
				java.util.List actorlst = nextStatus.getActorLst();
				for(java.util.Iterator it2=actorlst.iterator();it2.hasNext();)
				{
					DefActor dactor = (DefActor)it2.next();
					String actortype = dactor.getType();
					String actorId = dactor.getId();
					//条件选择参与者
					String condition = Util.fmtStr(dactor.getCondition());
					String conditionUserId = "";
					if("user".equals(actortype))
					{
						String userId = dpdutil.fmtStr(dactor.getId());
						if(!"".equals(userId))
						{
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
						else
						{
							String loginId = dpdutil.fmtStr(dactor.getLoginId());						
							IUser user = dpdutil.getUserNameByLoginId(loginId);
							if(user != null)
							{
								actorstr = actorstr + user.getId()+","+user.getName()+";";
								conditionUserId = conditionUserId + user.getId()+","+user.getName()+";";
							}
						}
					}
					if("org".equals(actortype))
					{
						java.util.List userlst = dpdutil.getOrgUserList(dactor.getId());
						for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
						{
							String userId = (String)it3.next();
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
					}
					if("role".equals(actortype))
					{
						java.util.List userlst = dpdutil.getRoleUserList(dactor.getId());
						log.debug("getSecondTask Workflow.................roleName="+dactor.getId()+",userlist="+userlst);
						for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
						{
							String userId = (String)it3.next();
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
					}
					if("variable".equals(actortype))
					{
						if("manager".equals(actorId))
						{
							String managerId = dpdutil.getManagerId(invokeUserId);
							String managerName = dpdutil.getUserName(managerId);
							
							actorstr = actorstr + managerId+","+managerName+";";
							conditionUserId = conditionUserId + managerId+","+managerName+";";
						}
						if("userid".equals(actorId))
						{
							actorstr = actorstr + "userid,userid;";
							
							List<DefActorSelect> actorSelList = dactor.getSelectLst();
							for(DefActorSelect das:actorSelList)
							{
								if("user".equalsIgnoreCase(das.getType()))
								{
									if(!"".equals(das.getId()))
									{
										String userName = dpdutil.getUserName(das.getId());
										actorsel = actorsel + das.getId()+","+userName+";";
										conditionUserId = conditionUserId + das.getId()+","+userName+";";
									}
									else
									{
										String loginId = dpdutil.fmtStr(das.getLoginId());						
										IUser user = dpdutil.getUserNameByLoginId(loginId);
										if(user != null)
										{
											actorsel = actorsel + user.getId()+","+user.getName()+";";
											conditionUserId = conditionUserId + user.getId()+","+user.getName()+";";
										}
									}
								}
								if("role".equalsIgnoreCase(das.getType()))
								{
									java.util.List userlst = dpdutil.getRoleUserList(das.getId());
									log.debug("getSecondTask Workflow Select.................roleName="+dactor.getId()+",userlist="+userlst);
									for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
									{
										String userId = (String)it3.next();
										String userName = dpdutil.getUserName(userId);
										actorsel = actorsel + userId+","+userName+";";
										conditionUserId = conditionUserId + userId+","+userName+";";
									}
								}
							}
						}
					}
					//条件选择参与者
					if(!"".equals(condition) && toStatus.getId().equals(Util.fmtStr(nexttask)))
					{
						Properties selUserProp = new Properties();
						selUserProp.setProperty("condition", condition);
						selUserProp.setProperty("conditionUserId", conditionUserId);
						selUserList.add(selUserProp);
					}
				}
			}
			prop.setProperty("actor",actorstr);
			prop.setProperty("actorsel",actorsel);
			rtn.add(prop);
			//条件选择参与者
			rtn.add(selUserList);
		}
		return rtn;
	}
	
	//通过节点名称查询工作流定义本节点参数
	public java.util.List getThisTaskByDef(String nexttask,String invokeUserId,String defProcessId,
			StatusMsg msg)throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		java.util.List rtn = new java.util.ArrayList();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return rtn;
		}
		//条件选择参与者
		List<Properties> selUserList = new ArrayList<Properties>();
		DefStatus nextStatus = defProcess.getStatus(nexttask);
		//取得下一环节参与者列表
		java.util.List actorlst = nextStatus.getActorLst();
		for(java.util.Iterator it2=actorlst.iterator();it2.hasNext();)
		{
			java.util.Properties prop = new java.util.Properties();
			String actorstr = "";
			String actorsel = "";	
			
			DefActor dactor = (DefActor)it2.next();
			String actortype = dactor.getType();
			String actorId = dactor.getId();
			//条件选择参与者
			String condition = Util.fmtStr(dactor.getCondition());
			if("user".equals(actortype))
			{
				String userId = dpdutil.fmtStr(dactor.getId());
				if(!"".equals(userId))
				{
					String userName = dpdutil.getUserName(userId);
					actorstr = actorstr + userId+","+userName+";";
				}
				else
				{
					String loginId = dpdutil.fmtStr(dactor.getLoginId());						
					IUser user = dpdutil.getUserNameByLoginId(loginId);
					if(user != null)
					{
						actorstr = actorstr + user.getId()+","+user.getName()+";";
					}
				}
			}
			if("org".equals(actortype))
			{
				java.util.List userlst = dpdutil.getOrgUserList(dactor.getId());
				for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
				{
					String userId = (String)it3.next();
					String userName = dpdutil.getUserName(userId);
					actorstr = actorstr + userId+","+userName+";";
				}
			}
			if("role".equals(actortype))
			{
				java.util.List userlst = dpdutil.getRoleUserList(dactor.getId());
				for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
				{
					String userId = (String)it3.next();
					String userName = dpdutil.getUserName(userId);
					actorstr = actorstr + userId+","+userName+";";
				}
			}
			if("variable".equals(actortype))
			{
				if("manager".equals(actorId))
				{
					String managerId = dpdutil.getManagerId(invokeUserId);
					String managerName = dpdutil.getUserName(managerId);
					
					actorstr = actorstr + managerId+","+managerName+";";
				}
				if("userid".equals(actorId))
				{
					actorstr = actorstr + "userid,userid;";
					
					List<DefActorSelect> actorSelList = dactor.getSelectLst();
					for(DefActorSelect das:actorSelList)
					{
						if("user".equalsIgnoreCase(das.getType()))
						{
							if(!"".equals(das.getId()))
							{
								String userName = dpdutil.getUserName(das.getId());
								actorsel = actorsel + das.getId()+","+userName+";";
							}
							else
							{
								String loginId = dpdutil.fmtStr(das.getLoginId());						
								IUser user = dpdutil.getUserNameByLoginId(loginId);
								if(user != null)
								{
									actorsel = actorsel + user.getId()+","+user.getName()+";";
								}
							}
						}
						if("role".equalsIgnoreCase(das.getType()))
						{
							java.util.List userlst = dpdutil.getRoleUserList(das.getId());
							for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
							{
								String userId = (String)it3.next();
								String userName = dpdutil.getUserName(userId);
								actorsel = actorsel + userId+","+userName+";";
							}
						}
					}
				}
			}
			prop.setProperty("actor",actorstr);
			prop.setProperty("actorsel",actorsel);
			prop.setProperty("condition",condition);
			rtn.add(prop);
		}
		return rtn;
	}

	//通过流程实例数据取得流程下一节点名称及参数
	public java.util.List getNextTask(String nexttask,String instWorkitemId,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		log.debug("instWorkitemId = "+instWorkitemId);
		java.util.List rtn = new java.util.ArrayList();
		DefWorkItem dwi = getWorkItem(instWorkitemId);
		if(dwi == null)
		{
			msg.setOk(false);
			msg.setCode("WF000014");
			msg.setMsg("任务 "+instWorkitemId+" 已处理");
			return rtn;
		}
		//条件选择参与者
		List<Properties> selUserList = new ArrayList<Properties>();
		String defProcessId = dwi.getProcessId();
		DefProcess defProcess = getProcess(defProcessId,msg);
		//log.debug("defProcess = "+defProcess);
		//获取下一环节
		DefStatus currentStatus = defProcess.getStatus(dwi.getStatusId());
		log.debug("currentStatus = "+currentStatus);
		if(currentStatus == null)
			throw new Exception("WF000005 下一节点为空");
		java.util.List tolst = currentStatus.getToStatusLst();
		log.debug("tolst = "+tolst);
		for(java.util.Iterator it=tolst.iterator();it.hasNext();)
		{
			DefToStatus toStatus = (DefToStatus)it.next();
			java.util.Properties prop = new java.util.Properties();
			prop.setProperty("id",toStatus.getId());
			prop.setProperty("name",toStatus.getName());
			prop.setProperty("type",Util.fmtStr(toStatus.getType()));
			String actorstr = "";
			String actorsel = "";
			if("false".equals(toStatus.getCondition()))
			{
				String nextId = toStatus.getId();
				DefStatus nextStatus = defProcess.getStatus(nextId);
				prop.setProperty("excluderole", dpdutil.fmtStr(nextStatus.getExcludeRole()));
				prop.setProperty("condition",Util.fmtStr(nextStatus.getCondition()));
				//取得下一环节参与者列表
				java.util.List actorlst = nextStatus.getActorLst();
				for(java.util.Iterator it2=actorlst.iterator();it2.hasNext();)
				{
					DefActor dactor = (DefActor)it2.next();
					String actortype = dactor.getType();
					//条件选择参与者
					String condition = Util.fmtStr(dactor.getCondition());
					String conditionUserId = "";
					if("user".equals(actortype))
					{					
						String userId = dpdutil.fmtStr(dactor.getId());				
						if(!"".equals(userId))
						{
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
						else
						{
							String loginId = dpdutil.fmtStr(dactor.getLoginId());						
							IUser user = dpdutil.getUserNameByLoginId(loginId);
							if(user != null)
							{
								actorstr = actorstr + user.getId()+","+user.getName()+";";
								conditionUserId = conditionUserId + user.getId()+","+user.getName()+";";
							}
						}						
					}
					if("org".equals(actortype))
					{
						java.util.List userlst = dpdutil.getOrgUserList(dactor.getId());
						for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
						{
							String userId = (String)it3.next();
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
					}
					if("role".equals(actortype))
					{
						java.util.List userlst = dpdutil.getRoleUserList(dactor.getId());
						log.debug("getNextTask Workflow.................roleName="+dactor.getId()+",userlist="+userlst);
						for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
						{
							String userId = (String)it3.next();
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
					}
					if("class".equals(actortype))
					{
						String actorid = dactor.getId();
						if(actorid == null || actorid.trim().equals(""))
							continue;
						IGetActor getactor = (IGetActor)Class.forName(actorid).newInstance();
						java.util.List userlst = getactor.getUserIdList(dwi.getStartUserId(),dwi.getUserId());
						for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
						{
							String userId = (String)it3.next();
							String userName = dpdutil.getUserName(userId);
							actorstr = actorstr + userId+","+userName+";";
							conditionUserId = conditionUserId + userId+","+userName+";";
						}
					}
					if("variable".equals(actortype))
					{
						String userId = dactor.getId();
						if("starter".equals(userId))
						{
							String userName = dpdutil.getUserName(dwi.getStartUserId());
							actorstr = actorstr + dwi.getStartUserId()+","+userName+";";
							conditionUserId = conditionUserId + dwi.getStartUserId()+","+userName+";";
						}
						if("manager".equals(userId))
						{
							String managerId = dpdutil.getManagerId(dwi.getUserId());
							String managerName = dpdutil.getUserName(managerId);
							actorstr = actorstr + managerId+","+managerName+";";
							conditionUserId = conditionUserId + managerId+","+managerName+";";
						}
						if("userid".equals(userId))
						{
							actorstr = actorstr + "userid,userid;";

							List<DefActorSelect> actorSelList = dactor.getSelectLst();
							for(DefActorSelect das:actorSelList)
							{
								if("user".equalsIgnoreCase(das.getType()))
								{
									if(!"".equals(das.getId()))
									{
										String userName = dpdutil.getUserName(das.getId());
										actorsel = actorsel + das.getId()+","+userName+";";
										conditionUserId = conditionUserId + das.getId()+","+userName+";";
									}
									else
									{
										String loginId = dpdutil.fmtStr(das.getLoginId());						
										IUser user = dpdutil.getUserNameByLoginId(loginId);
										if(user != null)
										{
											actorsel = actorsel + user.getId()+","+user.getName()+";";
											conditionUserId = conditionUserId + user.getId()+","+user.getName()+";";
										}
									}
								}
								if("role".equalsIgnoreCase(das.getType()))
								{
									java.util.List userlst = dpdutil.getRoleUserList(das.getId());
									log.debug("getNextTask Workflow Select.................roleName="+dactor.getId()+",userlist="+userlst);
									for(java.util.Iterator it3=userlst.iterator();it3.hasNext();)
									{
										String userId3 = (String)it3.next();
										String userName = dpdutil.getUserName(userId3);
										actorsel = actorsel + userId3+","+userName+";";
										conditionUserId = conditionUserId + userId3+","+userName+";";
									}
								}
							}
						}
					}
					//条件选择参与者
					if(!"".equals(condition) && toStatus.getId().equals(Util.fmtStr(nexttask)))
					{
						Properties selUserProp = new Properties();
						selUserProp.setProperty("condition", condition);
						selUserProp.setProperty("conditionUserId", conditionUserId);
						selUserList.add(selUserProp);
					}
				}
			}
			prop.setProperty("actor",actorstr);
			prop.setProperty("actorsel",actorsel);
			rtn.add(prop);
			//条件选择参与者
			rtn.add(selUserList);
		}
		log.debug("rtn = "+rtn);
		return rtn;
	}
	
	//通过流程实例数据取得流程下一节点名称及参数
	public java.util.List getThisTaskByInst(String nexttask,String instWorkitemId,StatusMsg msg)throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		log.debug("instWorkitemId = "+instWorkitemId);
		java.util.List rtn = new java.util.ArrayList();
		DefWorkItem dwi = getWorkItem(instWorkitemId);
		if(dwi == null)
		{
			msg.setOk(false);
			msg.setCode("WF000014");
			msg.setMsg("任务 "+instWorkitemId+" 已处理");
			return rtn;
		}
		//条件选择参与者
		List<Properties> selUserList = new ArrayList<Properties>();
		String defProcessId = dwi.getProcessId();
		rtn = getThisTaskByDef(nexttask,dwi.getUserId(),defProcessId,msg);
		return rtn;
	}
	
	//根据流程ID和节点ID取得流程触发应用
	public Properties getInvokeApp(String processId,String statusId,String nexttask,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		DefProcess defProcess = getProcess(processId,msg);
		//获取下一环节
		DefStatus currentStatus = defProcess.getStatus(statusId);
		java.util.List tolst = currentStatus.getToStatusLst();
		for(java.util.Iterator it=tolst.iterator();it.hasNext();)
		{
			DefToStatus toStatus = (DefToStatus)it.next();
			String toId = toStatus.getId();
			if(nexttask.equals(toId))
			{
				Properties prop = new Properties();
				prop.setProperty("invoke", Util.fmtStr(toStatus.getInvoke()));
				prop.setProperty("command", Util.fmtStr(toStatus.getInvokeCommand()));
				return prop;
			}
		}
		return null;
	}
	
	//instActivityId区分分系统标志flag和测试标志testMode
	//根据流程实例数据取得流程触发应用
	public Properties getInvokeApp2(String instProcessId,String instActivityId,
			String nexttask,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		String processId = this.getProcessId(instProcessId);
		if(processId == null)
			return null;
		String statusId = this.getStatusId(instActivityId);
		if(statusId == null)
			return null;
		return getInvokeApp(processId,statusId,nexttask,msg);
	}

	//instWorkitemId区分分系统标志flag和测试标志testMode
	/*
	*判断下一环节是否存在预定义参与者
	*/
	public boolean isNextTaskDefineActor(String instWorkitemId,StatusMsg msg)throws Exception
	{
		boolean rtn = false;
		if(msg == null)
			msg = new StatusMsg();
		DefWorkItem dwi = getWorkItem(instWorkitemId);
		String defProcessId = dwi.getProcessId();
		DefProcess defProcess = getProcess(defProcessId,msg);
		//获取下一环节
		DefStatus currentStatus = defProcess.getStatus(dwi.getStatusId());
		java.util.List tolst = currentStatus.getToStatusLst();
		for(java.util.Iterator it=tolst.iterator();it.hasNext();)
		{
			DefToStatus toStatus = (DefToStatus)it.next();
			String nextId = toStatus.getId();
			DefStatus nextStatus = defProcess.getStatus(nextId);
			//取得下一环节参与者列表
			java.util.List actorlst = nextStatus.getActorLst();
			//如果没有参与者等同存在预定义参与者
			if(actorlst == null || actorlst.size() == 0)
				return true;
			for(java.util.Iterator it2=actorlst.iterator();it2.hasNext();)
			{
				DefActor dactor = (DefActor)it2.next();
				String actortype = dactor.getType();
				if("user".equals(actortype))
				{					
					String userId = dpdutil.fmtStr(dactor.getId());
					if(!"".equals(userId))
					{
						String userName = dpdutil.getUserName(userId);
						if(userName != null)
							return true;
					}
					else
					{
						String loginId = dpdutil.fmtStr(dactor.getLoginId());
						IUser user = dpdutil.getUserNameByLoginId(loginId);
						if(user != null)
							return true;
					}					
				}
				if("org".equals(actortype))
				{
					java.util.List userlst = dpdutil.getOrgUserList(dactor.getId());
					if(userlst.size()>0)
						return true;
				}
				if("role".equals(actortype))
				{
					java.util.List userlst = dpdutil.getRoleUserList(dactor.getId());
					log.debug("isNextTaskDefineActor Workflow.................roleName="+dactor.getId()+",userlist="+userlst);
					if(userlst.size()>0)
						return true;
				}
				if("class".equals(actortype))
				{
					String actorid = dactor.getId();
					if(actorid == null || actorid.trim().equals(""))
						return false;
					IGetActor getactor = (IGetActor)Class.forName(actorid).newInstance();
					java.util.List userlst = getactor.getUserIdList(dwi.getStartUserId(),dwi.getUserId());
					if(userlst.size()>0)
						return true;
				}
			}
		}
		return rtn;
	}

	//需要区分分系统标志flag和测试标志testMode
	/**
	* defProcessId:流程模板ID
	* userId:流程启动者
	* instanceId:业务数据ID
	* type:业务标示,启动流程根据业务分类指定
	* title:待办件标题
	* nexttask:用于流程分支判断标志,其值为状态id
	* url:业务应用
	*/
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public StatusMsg startWorkflow(String defProcessId,String startUserId,String userId,String instanceId,String type,
			String title,String nexttask,String url,java.util.HashMap hmap,String flag2,String testMode) throws Exception
	{
		try
		{			
			StatusMsg msg = new StatusMsg();
			if("".equals(Util.fmtStr(startUserId)))
			{
				msg.setOk(false);
				msg.setCode("WF000006");
				msg.setMsg("启动用户(startUserId) "+startUserId+" 未定义");
				return msg;
			}
			DefProcess defProcess = checkValid(defProcessId,instanceId,type,title,url,msg);
			if(!msg.isOk())
				return msg;
			DefStatus defStartStatus = defProcess.getStart();
			if(defStartStatus == null)
			{
				msg.setOk(false);
				msg.setCode("WF000007");
				msg.setMsg("工作流模板 "+defProcessId+" 未定义开始状态,请检查模板文件:"+defProcessId+".xml");
				return msg;
			}
			java.util.List userIdLst = new java.util.ArrayList();
			userIdLst.add(userId);		
			
			//工作流前置触发应用
			Properties beforeProp = getInvokeApp(defProcessId,defStartStatus.getId(),
					nexttask,msg);
			if(!msg.isOk())
				return msg;
			if(beforeProp != null)
			{
				String clz = beforeProp.getProperty("invoke");
				String command = beforeProp.getProperty("command");
				if(!"".equals(clz))
				{
					StatusMsg rtn = beforeInvokeApp(clz,command,defProcess.getId(),type,instanceId,
						startUserId,userIdLst,title,url,defStartStatus.getId(),nexttask,flag2,testMode);
					if(rtn != null && !rtn.isOk())
						return rtn;
				}
			}
			
			String xmlData = "";
			if(hmap != null)
			{
				xmlData = xmlData + "<?xml version=\"1.0\" encoding=\"gb2312\"?><data>";
				java.util.Set keyset = hmap.keySet();
				for(java.util.Iterator it=keyset.iterator();it.hasNext();)
				{
					Object key = it.next();
					Object value = hmap.get(key);
					xmlData = xmlData + "<"+key+"><![CDATA["+value+"]]></"+key+">";
				}
				xmlData = xmlData + "</data>";
			}
	
			String instProcessId = dpdutil.createId("process");
			java.sql.Timestamp finishTime = null;
			String processName = defProcess.getName();
			String statusId = defStartStatus.getId();
			String statusName = defStartStatus.getName();
			//添加开始节点待办件(已办结)
	
			String startWorkItemId = dpdutil.createId("workitem");
			String startInstActivityId = dpdutil.createId("activity");
			//如果传入url为空，取工作流节点定义的url
			String startUrl = null;
			if(url == null)
				startUrl = getUrl("Start",defProcessId);
			else
				startUrl = url;
			//2016/04/04
			//表单A创建工作流时,开始节点不产生待办件，如果第二个节点表单B与表单A不是同一实体，
			//在开始节点办结件需要记录表单A的实体ID,通过originalid传入
			//参考验货单流程
			//开始(验货单)------>入库(入库单)--------->结束(验货单)
			String originalId = instanceId;
			Object originalObj = hmap.get("originalid");
			if(originalObj != null)
			{
				originalId = (String)originalObj;
			}
			//--------------------------------------------end
			//创建开始节点办结记录
			DefWorkItem dwi = saveWorkItem(startWorkItemId,instProcessId,startInstActivityId,startUserId,
					startUserId,new java.sql.Timestamp(new java.util.Date().getTime()-60*1000),
					new java.sql.Timestamp(new java.util.Date().getTime()-60*1000),
					defProcessId,processName,title,originalId,startUrl,statusId,statusName,
					type,xmlData,"done",flag2,testMode);
			
			//记录instProcessId,instActivityId,instWorkitemId到wfMsg,以备保存意见
			msg.setInstProcessId(dwi.getInstProcessId());
			msg.setInstActivityId(dwi.getInstActivityId());
			msg.setInstWorkitemId(dwi.getId());
			msg.setInstanceId(instanceId);
	
	        String newInstActivityId = dpdutil.createId("activity");
	        //如果传入url为空，取工作流节点定义的url
	        String nextUrl = null;
	        if(url == null)
	        	nextUrl = getUrl(nexttask,defProcessId);
	        else
	        	nextUrl = url;
	        //创建下一环节待办件
	        java.util.List<DefWorkItem> dwiList = createNewWorkitem(defProcess,instProcessId,
	        		newInstActivityId,null,statusId,startUserId,
	        		startUserId,userIdLst,instanceId,type,title,
	        		nexttask,nextUrl,xmlData,null,flag2,testMode);         
			
			//如果流转结束添加结束待办件(已办结)
			DefStatus endStatus = defProcess.getStatus(nexttask);
			//if(endStatus != null && "end".equals(endStatus.getId()))
			if(endStatus != null && endStatus.getId().startsWith("end"))
			{
				String endWorkItemId = dpdutil.createId("workitem");
				String endInstActivityId = dpdutil.createId("activity");
				//如果传入url为空，取工作流节点定义的url
				String endUrl = null;
				if(url == null)
					endUrl = getUrl("end",defProcessId);
				else
					endUrl = url;
				//生成办结记录
				saveWorkItem(endWorkItemId,instProcessId,endInstActivityId,endStatus.getId(),startUserId,new java.sql.Timestamp(new java.util.Date().getTime()+60*1000),
						new java.sql.Timestamp(new java.util.Date().getTime()+60*1000),
						defProcessId,processName,title,instanceId,endUrl,endStatus.getId(),
						endStatus.getName(),type,xmlData,"done",flag2,testMode);
				
		        //工作流后置触发应用
		        Properties afterProp = getInvokeApp2(instProcessId,startInstActivityId,nexttask,msg);	        
		        if(afterProp != null)
		        {
		            String clz = beforeProp.getProperty("invoke");
		            String command = beforeProp.getProperty("command");
		            if(!"".equals(clz))
		            {                               
		                afterInvokeApp(clz,command,defProcess.getId(),type,instanceId,
		                                instProcessId,newInstActivityId,dwiList,startUserId,userIdLst,
		                                title,url,"start",nexttask,flag2,testMode);
		            }
		        }
			}
			else
			{
		        //工作流后置触发应用
		        Properties afterProp = getInvokeApp2(instProcessId,startInstActivityId,nexttask,msg);	        
		        if(afterProp != null)
		        {
		            String clz = beforeProp.getProperty("invoke");
		            String command = beforeProp.getProperty("command");
		            if(!"".equals(clz))
		            {                               
		                afterInvokeApp(clz,command,defProcess.getId(),type,instanceId,
		                                instProcessId,newInstActivityId,dwiList,startUserId,userIdLst,
		                                title,url,"start",nexttask,flag2,testMode);
		            }
		        }
			}
	
			//删除undo待办件
			//deleteUndoWorkItem(instanceId);
			
			//完成开始undo待办件
			finishStartWorkItem(instanceId);
			return msg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//需要区分分系统标志flag和测试标志testMode
	/**
	* defProcessId:流程模板ID
	* userId:流程启动者
	* instanceId:业务数据ID
	* type:业务标示,启动流程根据业务分类指定
	* title:待办件标题
	* nexttask:用于流程分支判断标志,其值为状态id
	* url:业务应用
	*/
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public StatusMsg startWorkflow(String defProcessId,String startUserId,
			java.util.List userIdLst,String instanceId,String type,String title,
			String nexttask,String url,java.util.HashMap hmap,
			String flag2,String testMode) throws Exception
	{
		try
		{			
			StatusMsg msg = new StatusMsg();
			if("".equals(Util.fmtStr(startUserId)))
			{
				msg.setOk(false);
				msg.setCode("WF000006");
				msg.setMsg("启动用户(startUserId) "+startUserId+" 未定义");
				return msg;
			}
			DefProcess defProcess = checkValid(defProcessId,instanceId,type,title,url,msg);
			if(!msg.isOk())
				return msg;
			DefStatus defStartStatus = defProcess.getStart();
			if(defStartStatus == null)
			{
				msg.setOk(false);
				msg.setCode("WF000007");
				msg.setMsg("工作流模板 "+defProcessId+" 未定义开始状态,请检查模板文件:"+defProcessId+".xml");
				return msg;
			}
			
			//工作流前置触发应用
			Properties beforeProp = getInvokeApp(defProcessId,defStartStatus.getId(),
					nexttask,msg);
			if(!msg.isOk())
				return msg;
			if(beforeProp != null)
			{
				String clz = beforeProp.getProperty("invoke");
				String command = beforeProp.getProperty("command");
				if(!"".equals(clz))
				{
					StatusMsg rtn = beforeInvokeApp(clz,command,defProcess.getId(),type,instanceId,
						startUserId,userIdLst,title,url,defStartStatus.getId(),nexttask,flag2,testMode);
					if(rtn != null && !rtn.isOk())
						return rtn;
				}
			}
			
			String xmlData = "";
			if(hmap != null)
			{
				xmlData = xmlData + "<?xml version=\"1.0\" encoding=\"gb2312\"?><data>";
				java.util.Set keyset = hmap.keySet();
				for(java.util.Iterator it=keyset.iterator();it.hasNext();)
				{
					Object key = it.next();
					Object value = hmap.get(key);
					xmlData = xmlData + "<"+key+"><![CDATA["+value+"]]></"+key+">";
				}
				xmlData = xmlData + "</data>";
			}
	
			String instProcessId = dpdutil.createId("process");
			java.sql.Timestamp finishTime = null;
			String processName = defProcess.getName();
			String statusId = defStartStatus.getId();
			String statusName = defStartStatus.getName();
			//添加开始节点待办件(已办结)
	
			String startWorkItemId = dpdutil.createId("workitem");
			String startInstActivityId = dpdutil.createId("activity");
			//如果传入url为空，取工作流节点定义的url
			String startUrl = null;
			if(url == null)
				startUrl = getUrl("Start",defProcessId);
			else
				startUrl = url;
			//2016/04/04
			//表单A创建工作流时,开始节点不产生待办件，如果第二个节点表单B与表单A不是同一实体，
			//在开始节点办结件需要记录表单A的实体ID,通过originalid传入
			//参考验货单流程
			//开始(验货单)------>入库(入库单)--------->结束(验货单)
			String originalId = instanceId;
			Object originalObj = hmap.get("originalid");
			if(originalObj != null)
			{
				originalId = (String)originalObj;
			}
			//--------------------------------------------end
			//创建开始节点办结记录
			DefWorkItem dwi = saveWorkItem(startWorkItemId,instProcessId,startInstActivityId,startUserId,
					startUserId,new java.sql.Timestamp(new java.util.Date().getTime()-60*1000),
					new java.sql.Timestamp(new java.util.Date().getTime()-60*1000),
					defProcessId,processName,title,originalId,startUrl,statusId,statusName,
					type,xmlData,"done",flag2,testMode);
			
			//记录instProcessId,instActivityId,instWorkitemId到wfMsg,以备保存意见
			msg.setInstProcessId(dwi.getInstProcessId());
			msg.setInstActivityId(dwi.getInstActivityId());
			msg.setInstWorkitemId(dwi.getId());
			msg.setInstanceId(instanceId);
	
	        String newInstActivityId = dpdutil.createId("activity");
	        //如果传入url为空，取工作流节点定义的url
	        String nextUrl = null;
	        if(url == null)
	        	nextUrl = getUrl(nexttask,defProcessId);
	        else
	        	nextUrl = url;
	        //创建下一环节待办件
	        java.util.List<DefWorkItem> dwiList = createNewWorkitem(defProcess,instProcessId,
	        		newInstActivityId,null,statusId,startUserId,
	        		startUserId,userIdLst,instanceId,type,title,
	        		nexttask,nextUrl,xmlData,null,flag2,testMode);         
			
			//如果流转结束添加结束待办件(已办结)
			DefStatus endStatus = defProcess.getStatus(nexttask);
			//if(endStatus != null && "end".equals(endStatus.getId()))
			if(endStatus != null && endStatus.getId().startsWith("end"))
			{
				String endWorkItemId = dpdutil.createId("workitem");
				String endInstActivityId = dpdutil.createId("activity");
				//如果传入url为空，取工作流节点定义的url
				String endUrl = null;
				if(url == null)
					endUrl = getUrl("end",defProcessId);
				else
					endUrl = url;
				//生成办结记录
				saveWorkItem(endWorkItemId,instProcessId,endInstActivityId,endStatus.getId(),startUserId,new java.sql.Timestamp(new java.util.Date().getTime()+60*1000),
						new java.sql.Timestamp(new java.util.Date().getTime()+60*1000),
						defProcessId,processName,title,instanceId,endUrl,endStatus.getId(),
						endStatus.getName(),type,xmlData,"done",flag2,testMode);
				
		        //工作流后置触发应用
		        Properties afterProp = getInvokeApp2(instProcessId,startInstActivityId,nexttask,msg);	        
		        if(afterProp != null)
		        {
		            String clz = beforeProp.getProperty("invoke");
		            String command = beforeProp.getProperty("command");
		            if(!"".equals(clz))
		            {                               
		                afterInvokeApp(clz,command,defProcess.getId(),type,instanceId,
		                                instProcessId,newInstActivityId,dwiList,startUserId,userIdLst,
		                                title,url,"start",nexttask,flag2,testMode);
		            }
		        }
			}
			else
			{
		        //工作流后置触发应用
		        Properties afterProp = getInvokeApp2(instProcessId,startInstActivityId,nexttask,msg);	        
		        if(afterProp != null)
		        {
		            String clz = beforeProp.getProperty("invoke");
		            String command = beforeProp.getProperty("command");
		            if(!"".equals(clz))
		            {                               
		                afterInvokeApp(clz,command,defProcess.getId(),type,instanceId,
		                                instProcessId,newInstActivityId,dwiList,startUserId,userIdLst,
		                                title,url,"start",nexttask,flag2,testMode);
		            }
		        }
			}
	
			//删除undo待办件
			//deleteUndoWorkItem(instanceId);
			
			//完成开始undo待办件
			finishStartWorkItem(instanceId);
			return msg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public StatusMsg submitWorkflow(String instProcessId,String instActivityId,
			String instWorkitemId,java.util.List userIdLst,String instanceId,
			String type,String title,String nexttask,String url,
			java.util.HashMap hmap,String flag2,String testMode) throws Exception
	{
		System.out.println("instProcessId==="+instProcessId);
		System.out.println("instActivityId==="+instActivityId);
		System.out.println("instWorkitemId==="+instWorkitemId);
		System.out.println("userIdLst==="+userIdLst);
		System.out.println("instanceId==="+instanceId);
		System.out.println("type==="+type);
		System.out.println("title==="+title);
		System.out.println("nexttask==="+nexttask);
		System.out.println("hmap==="+hmap);
		try
		{
			StatusMsg msg = new StatusMsg();
			//记录instProcessId,instActivityId,instWorkitemId到wfMsg,以备保存意见
			msg.setInstProcessId(instProcessId);
			msg.setInstActivityId(instActivityId);
			msg.setInstWorkitemId(instWorkitemId);
			msg.setInstanceId(instanceId);
			DefWorkItem dwi = getWorkItem(instWorkitemId);
			//判断是否异步待办件，已经不处理掉		
			if(dwi == null)
			{
				String prcessUserName = getProcessUserId(instProcessId,instActivityId);
				msg.setOk(false);
				msg.setCode("WF000015");
				msg.setMsg("任务已被用户:"+prcessUserName+" 处理完成,可忽略此待办工作");
				return msg;
			}
			
			title = dwi.getTitle();
			String defProcessId = dwi.getProcessId();
			DefProcess defProcess = checkValid(defProcessId,instanceId,type,title,url,msg);
			if(!msg.isOk())
				return msg;
			//检查此待办件是否已经处理
			if(dwi.getFinishTime() != null && !"Start".equals(dwi.getStatusId()))
			{
				msg.setOk(false);
				msg.setCode("WF000014");
				String prcessUserName = getProcessUserId(instProcessId,instActivityId);
				msg.setMsg("任务 "+dwi.getStatusName()+":已被用户:"+prcessUserName+" 已处理,可忽略此待办工作");
				return msg;
			}
			//工作流前置触发应用
			Properties beforeProp = getInvokeApp2(instProcessId,instActivityId,nexttask,msg);
			if(!msg.isOk())
				return msg;		
			if(beforeProp != null)
			{
				String clz = beforeProp.getProperty("invoke");
				String command = beforeProp.getProperty("command");
				if(!"".equals(clz))
				{
					StatusMsg rtn = beforeInvokeApp(clz,command,defProcess.getId(),type,instanceId,
							dwi.getUserId(),userIdLst,title,url,dwi.getStatusId(),nexttask,flag2,testMode);
					if(rtn != null && !rtn.isOk())
						return rtn;
				}
			}
			
			//完成本待办件
			String xmlData = "";
			if(hmap != null)
			{
				xmlData = xmlData + "<?xml version=\"1.0\" encoding=\"gb2312\"?><data>";
				java.util.Set keyset = hmap.keySet();
				for(java.util.Iterator it=keyset.iterator();it.hasNext();)
				{
					Object key = it.next();
					Object value = hmap.get(key);
					xmlData = xmlData + "<"+key+"><![CDATA["+value+"]]></"+key+">";
				}
				xmlData = xmlData + "</data>";
			}
			//办结本待办件记录，如果本待办件是开始节点的，开始时间和结束时间可能不同
			saveWorkItem(dwi.getId(),instProcessId,instActivityId,dwi.getStartUserId(),
				dwi.getUserId(),null,new java.sql.Timestamp(new java.util.Date().getTime()),
				dwi.getProcessId(),dwi.getProcessName(),title,dwi.getInstanceId(),dwi.getUrl(),
				dwi.getStatusId(),dwi.getStatusName(),type,xmlData,"done",flag2,testMode);
			//检查是否转移状态
			//1.如果存在本节点待办件并且为同步状态,必须等待全部待办件结束后方可状态转移
			//2.如果存在本节点待办件并且为异步状态,执行状态转移,并作废其他本节点代表件
			
			DefStatus defStatus = getStatusByInstActivityId(instActivityId,msg);
			String statusType = defStatus.getType();
			boolean isNextLogic = true;
			if("synchronized".equals(statusType))
			{
				int sameNum = getSameActivityWorkitem(instProcessId,instActivityId,msg);
				if(sameNum>0)
					isNextLogic = false;
			}
			if("asynchronized".equals(statusType))
			{
				//异步被处理的待办件，不删除待办件，置workitem的flag为pass
				deleteSameActivityWorkitem(instProcessId,instActivityId,msg);
				isNextLogic = true;
			}
			//如果异步处理或者同步节点符合办理数量，执行下一步
			if(isNextLogic)
			{
				String newInstActivityId = dpdutil.createId("activity");
				//如果传入url为空，取工作流节点定义的url
				String nextUrl = null;
				if(url == null)
					nextUrl = getUrlByInstWorkitemId(nexttask,instWorkitemId);
				else
					nextUrl = url;
				java.util.List<DefWorkItem> dwiList = createNewWorkitem(defProcess,instProcessId,newInstActivityId,instWorkitemId,
				dwi.getStatusId(),dwi.getStartUserId(),dwi.getUserId(),
				userIdLst,instanceId,type,title,nexttask,
				nextUrl,xmlData,null,flag2,testMode);
                        
                //工作流后置触发应用
                Properties afterProp = getInvokeApp2(instProcessId,instActivityId,nexttask,msg);	
                if(afterProp != null)
                {
                    String clz = beforeProp.getProperty("invoke");
                    String command = beforeProp.getProperty("command");
                    if(!"".equals(clz))
                    {
                        afterInvokeApp(clz,command,defProcess.getId(),type,instanceId,
                                        instProcessId,newInstActivityId,dwiList,dwi.getUserId(),userIdLst,
                                        title,url,dwi.getStatusId(),nexttask,flag2,testMode);
                    }
                }                        
				
				//如果流转结束添加结束待办件(已办结)
				DefStatus endStatus = defProcess.getStatus(nexttask);
				//if(endStatus != null && "end".equals(endStatus.getId()))
				if(endStatus != null && endStatus.getId().startsWith("end"))
				{
					String newWorkItemId = dpdutil.createId("workitem");
					//如果传入url为空，取工作流节点定义的url
					String endUrl = null;
					if(url == null)
						endUrl = getUrl("end",defProcessId);
					else
						endUrl = url;
					saveWorkItem(newWorkItemId,instProcessId,endStatus.getId(),endStatus.getId(),dwi.getUserId(),null,new java.sql.Timestamp(new java.util.Date().getTime()),
							defProcessId,defProcess.getName(),title,instanceId,endUrl,endStatus.getId(),
							endStatus.getName(),type,xmlData,"done",flag2,testMode);
				}
				if(endStatus != null && "destory".equals(endStatus.getId()))
				{
					String newWorkItemId = dpdutil.createId("workitem");
					//如果传入url为空，取工作流节点定义的url
					String destUrl = null;
					if(url == null)
						destUrl = getUrl("destory",defProcessId);
					else
						destUrl = url;
					saveWorkItem(newWorkItemId,instProcessId,endStatus.getId(),endStatus.getId(),dwi.getUserId(),null,
							new java.sql.Timestamp(new java.util.Date().getTime()),
							defProcessId,defProcess.getName(),title,instanceId,destUrl,endStatus.getId(),
							endStatus.getName(),type,xmlData,"done",flag2,testMode);
				}
			}
			//删除undo待办件
			//deleteUndoWorkItem(instanceId);
			
			//完成开始undo待办件
			finishStartWorkItem(instanceId);
			return msg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	//需要区分分系统标志flag和测试标志testMode
	private java.util.List<DefWorkItem> createNewWorkitem(DefProcess defProcess,String instProcessId,String instActivityId,String instWorkitemId,String statusId,
		String startUserId,String fromUserId,java.util.List userIdLst,String instanceId,String type,String title,String nexttask,
		String url,String xmlData,String flag,String flag2,String testMode) throws Exception
	{
		System.out.println("createNewWorkitem instProcessId="+instProcessId);
		System.out.println("createNewWorkitem instActivityId="+instActivityId);
		System.out.println("createNewWorkitem instWorkitemId="+instWorkitemId);
		System.out.println("createNewWorkitem statusId="+statusId);
		System.out.println("createNewWorkitem startUserId="+startUserId);
		System.out.println("createNewWorkitem fromUserId="+fromUserId);
		System.out.println("createNewWorkitem userIdLst="+userIdLst);
		System.out.println("createNewWorkitem instanceId="+instanceId);
		java.util.List<DefWorkItem> dwiList = new java.util.ArrayList<DefWorkItem>();
		//String newInstActivityId = dpdutil.createId("activity");
		//获取下一环节
		DefStatus currentStatus = defProcess.getStatus(statusId);
		java.util.List tolst = currentStatus.getToStatusLst();
		for(java.util.Iterator it=tolst.iterator();it.hasNext();)
		{
			DefToStatus toStatus = (DefToStatus)it.next();
			if("true".equals(toStatus.getCondition()))
			{
				String nextId = toStatus.getId();
				java.util.List<DefWorkItem> tempList = nextToDoLogic(defProcess,toStatus,instProcessId,instActivityId,
						instWorkitemId,toStatus,startUserId,fromUserId,userIdLst,
						instanceId,type,title,nexttask,url,xmlData,flag,flag2,testMode);
                                dwiList.addAll(tempList);
			}
			if("false".equals(toStatus.getCondition()))
			{
				if(nexttask.equals(toStatus.getId()))
				{
					java.util.List<DefWorkItem> tempList = nextToDoLogic(defProcess,toStatus,instProcessId,instActivityId,
							instWorkitemId,toStatus,startUserId,fromUserId,userIdLst,
							instanceId,type,title,nexttask,url,xmlData,flag,flag2,testMode);
                                        dwiList.addAll(tempList);
				}
			}
		}
		return dwiList;
	}

	//需要区分分系统标志flag和测试标志testMode
	private java.util.List<DefWorkItem> nextToDoLogic(DefProcess defProcess,DefToStatus toStatus2,
			String instProcessId,String instActivityId,String originalInstWorkitemId,
			DefToStatus toStatus,String startUserId,String fromUserId,
			java.util.List userIdLst,String instanceId,String type,String title,
			String nexttask,String url,String xmlData,String flag,String flag2,String testMode) throws Exception
	{
		java.util.List<DefWorkItem> dwiList = new java.util.ArrayList<DefWorkItem>();
		String defProcessId = defProcess.getId();
		String nextId = toStatus.getId();
		DefStatus nextStatus = defProcess.getStatus(nextId);
		//取得下一环节参与者列表
		for(java.util.Iterator it2=userIdLst.iterator();it2.hasNext();)
		{
			String userId = (String)it2.next();
			String id = dpdutil.createId("workitem");
			java.sql.Timestamp finishTime = null;
			String processName = defProcess.getName();
			String statusId = nextStatus.getId();
			String statusName = nextStatus.getName();
			DefWorkItem dwi = saveWorkItem(id,instProcessId,instActivityId,startUserId,userId,
					null,finishTime,defProcessId,processName,title,instanceId,url,statusId,
					statusName,type,xmlData,null,flag2,testMode);
                        dwiList.add(dwi);
		}
		return dwiList;
	}

	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getUndoWorkitemList(String userId,String testMode) throws Exception
	{
		java.util.List lst = null;
		lst = mapper.getUndoWorkitemList(userId,testMode,"undo");
		fillData(lst);
		return lst;
	}

	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getWorkitemList(String userId,String testMode) throws Exception
	{
		java.util.List lst = null;
		lst = mapper.getWorkitemList(userId,testMode);
		fillData(lst);
		return lst;
	}

	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getFinishList(String userId,String testMode) throws Exception
	{
		java.util.List lst = null;
		lst = mapper.getFinishList(userId,testMode);
		fillData(lst);
		return lst;
	}
	
	//instProcessId区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getTraceList(String instProcessId) throws Exception
	{
		java.util.List lst = null;
		lst = mapper.getFinishList(instProcessId,"pass");
		fillData(lst);
		return lst;
	}
	
	//instanceId区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getTraceListByInstanceId(String instanceId) throws Exception
	{
		java.util.List<DefWorkItem> lst = null;
		lst = mapper.getTraceListByInstanceId(instanceId,"pass");
		if(lst.size()==0)
			return lst;
		DefWorkItem dwi = lst.get(0);
		return getTraceList(dwi.getInstProcessId());
		
	}

	//需要区分分系统标志flag和测试标志testMode
	public PagedWorkItem getQueryWorkitemList(String userId,int countPerPage,int page,
			String xmlData,String startUserId,String title,java.sql.Timestamp createTime1,
			java.sql.Timestamp createTime2,String statusName,String testMode) throws Exception
	{
		PagedWorkItem pwi = new PagedWorkItem();
		PageHelper.startPage(page, countPerPage);
		java.util.List lst = mapper.getQueryWorkitemList(userId, xmlData, startUserId,
				title, createTime1, createTime2, statusName, testMode);
		PageInfo pi = new PageInfo(lst);
		pwi.setCount(pi.getTotal());
		pwi.setWorkItemList(pi.getList());
		pwi.setCurrentPage(page);
		return pwi;
	}

	//需要区分分系统标志flag和测试标志testMode
	public PagedWorkItem getQueryFinishList(String userId,int countPerPage,int page,
			String xmlData,String startUserId,String title,java.sql.Timestamp createTime1,
			java.sql.Timestamp createTime2,String statusName,String startUserName,String testMode) throws Exception
	{
		PagedWorkItem pwi = new PagedWorkItem();
		PageHelper.startPage(page, countPerPage);
		java.util.List lst = mapper.getQueryFinishList(userId, xmlData, startUserId,
				title, createTime1, createTime2, statusName, testMode);
		PageInfo pi = new PageInfo(lst);
		pwi.setCount(pi.getTotal());
		pwi.setWorkItemList(pi.getList());
		pwi.setCurrentPage(page);
		return pwi;
	}

	//workitemId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public DefWorkItem getWorkItem(String workitemId) throws Exception
	{
		DefWorkItem wi = mapper.getWorkitemById(workitemId);
		return wi;
	}
	
	//instProcessId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public String getProcessId(String instProcessId) throws Exception
	{
		List<String> lst = mapper.getProcessId(instProcessId);
		if(lst != null && lst.size()>0)
			return lst.get(0);
		return null;
	}
	
	//instActivityId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public String getStatusId(String instActivityId) throws Exception
	{
		String statusId = mapper.getStatusId(instActivityId);
		return statusId;
	}
	
	//instActivityId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public String getProcessUserId(String instProcessId,String instActivityId) throws Exception
	{
		String userId = mapper.getProcessUserId(instProcessId, instActivityId);
		String userName = dpdutil.getUserName(userId);
		return userName;
	}
	
	//通过工作流定义取得某节点表单域属性定义列表
	public DefStatus getStatus(String nexttask,String defProcessId) throws Exception
	{
		StatusMsg msg = new StatusMsg();
		java.util.List<DefField> rtn = new java.util.ArrayList<DefField>();
		DefProcess defProcess = getProcess(defProcessId,msg);
		if(!msg.isOk())
		{
			return null;
		}
		java.util.List lst = defProcess.getStatusLst();
		for(Iterator it=lst.iterator();it.hasNext();)
		{
			DefStatus ds = (DefStatus)it.next();
			if(ds.getId().equals(nexttask))
				return ds;
		}
		return null;
	}

	//instActivityId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public String getWorkflowByStatusId(String processId,String instWorkitemId,String statusId,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		if(statusId == null)
		{
			msg.setMsg("工作流办理节点未匹配......");
			msg.setOk(false);
			return null;
		}
		if(instWorkitemId != null)
		{
			DefWorkItem dwi = getWorkItem(instWorkitemId);
			if(dwi != null)
				processId = dwi.getProcessId();
		}
		DefStatus ds = getStatus(statusId,processId);
		if(ds != null)
			return ds.getWorkflow();
		return null;
	}
	
	//instActivityId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public int getSameActivityWorkitem(String instProcessId,String instActivityId,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		List<DefWorkItem> lst = mapper.getSameActivityWorkitem(instProcessId, instActivityId);
		return lst.size();
	}
	
	//instActivityId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public void deleteSameActivityWorkitem(String instProcessId,String instActivityId,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		List<DefWorkItem> lst = mapper.getSameActivityWorkitem(instProcessId, instActivityId);
		for(DefWorkItem dwi:lst)
		{
			dwi.setFinishTime(new java.sql.Timestamp(new Date().getTime()));
			dwi.setFlag("pass");
			mapper.updateWorkitem(dwi);
		}
	}
	
	//instActivityId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public DefStatus getStatusByInstActivityId(String instActivityId,StatusMsg msg) throws Exception
	{
		if(msg == null)
			msg = new StatusMsg();
		List<DefWorkItem> lst = mapper.getWorkitemListByInstActivityId(instActivityId);
		if(lst == null || lst.size() == 0)
			return null;
		DefWorkItem dwi = lst.get(0);
		String processId = dwi.getProcessId();
		String statusId = dwi.getStatusId();
		DefProcess defProcess = getProcess(processId,msg);
		if(defProcess != null)
		{
			List<DefStatus> statLst = defProcess.getStatusLst();
			for(DefStatus stat:statLst)
			{
				if(statusId.equals(stat.getId()))
				{
					return stat;
				}
			}
		}
		return null;
	}

	//workitemId 可以区分分系统标志flag和测试标志testMode
	public String getStatusName(String workitemId) throws Exception
	{
		if(workitemId == null)
			return null;
		DefWorkItem dwi = getWorkItem(workitemId);
		if(dwi == null)
			return null;
		return dwi.getStatusName();
	}

	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public String getLastStatusName(String instanceId) throws Exception
	{
		String statusName = mapper.getLastStatusName(instanceId);
		return statusName;
	}

	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getWorkItemList(String instanceId)
	{
		return mapper.getWorkItemListByInstanceId(instanceId);
	}
	
	//instProcessId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public java.util.List getWorkItemListByInstProcessId(String instProcessId)
	{
		return mapper.getWorkItemListByInstProcessId(instProcessId);
	}

	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	private DefWorkItem saveWorkItem(String id,String instProcessId,String instActivityId,String startUserId,String userId,
			java.sql.Timestamp createTime,java.sql.Timestamp finishTime,String processId,String processName,String title,String instanceId,String url,
		String statusId,String statusName,String type,String xmlData,String flag,String flag2,String testMode) throws Exception
	{
		DefWorkItem wi = null;
		boolean isnew = false;
		wi = mapper.getWorkitemById(id);
		if(wi == null)
		{
			wi = new DefWorkItem();
			isnew = true;
		}	
		wi.setId(id);
		wi.setInstProcessId(instProcessId);
		wi.setInstActivityId(instActivityId);
		wi.setStartUserId(startUserId);
		wi.setUserId(userId);
		if(createTime != null)
			wi.setCreateTime(createTime);
		wi.setFinishTime(finishTime);
		wi.setProcessId(processId);
		wi.setProcessName(processName);
		wi.setTitle(title);
		wi.setInstanceId(instanceId);
		wi.setUrl(url);
		wi.setStatusId(statusId);
		wi.setStatusName(statusName);
		wi.setType(type);
		wi.setXmlData(xmlData);
		wi.setFlag(flag);
		wi.setFlag2(flag2);
		wi.setTestMode(testMode);
		if(isnew)
			mapper.saveWorkitem(wi);
		else
			mapper.updateWorkitem(wi);
		return wi;
	}

	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public void saveUndoWorkItem(String processId,String processName,String userId,String title,String url,
			String instanceId,java.util.HashMap hmap,String flag2,String testMode) throws Exception
	{	
		DefWorkItem wi = null;
		String xmlData = "";
		if(hmap != null)
		{
			xmlData = xmlData + "<?xml version=\"1.0\" encoding=\"gb2312\"?><data>";
			java.util.Set keyset = hmap.keySet();
			for(java.util.Iterator it=keyset.iterator();it.hasNext();)
			{
				Object key = it.next();
				Object value = hmap.get(key);
				xmlData = xmlData + "<"+key+"><![CDATA["+value+"]]></"+key+">";
			}
			xmlData = xmlData + "</data>";
		}
		//instanceId 可以区分分系统标志flag和测试标志testMode
		java.util.List lst = mapper.getDraftWorkItemListByInstanceId(instanceId,flag2);
		java.util.Iterator it = lst.iterator();
		boolean isnew = true;
		if(it.hasNext())
		{
			wi = (DefWorkItem)it.next();
			isnew = false;
		}
		else
		{
			wi = new DefWorkItem();
			String id = dpdutil.createId("workitem");
			wi.setId(id);
			wi.setProcessId(processId);
			wi.setProcessName(processName);
			wi.setStatusId("draft");
			wi.setStatusName("草稿");
			wi.setUserId(userId);
			wi.setTitle(title);
			wi.setInstanceId(instanceId);
			wi.setUrl(url);
			wi.setFlag("undo");
			wi.setFlag2(flag2);
			wi.setTestMode(testMode);
		}
		wi.setXmlData(xmlData);
		if(isnew)
			mapper.saveWorkitem(wi);
		else
			mapper.updateWorkitem(wi);
	}
	
	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public DefWorkItem getStartWorkItem(String instanceId) throws Exception
	{	
		DefWorkItem wi = null;
		java.util.List lst = mapper.getWorkItemListByInstanceIdAndStatusId(instanceId, "Start");	
		java.util.Iterator it = lst.iterator();
		boolean isnew = true;
		if(it.hasNext())
		{
			return (DefWorkItem)it.next();
		}
		return null;
	}
	
	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public String getInstProcessIdByInstanceId(String instanceId) throws Exception
	{	
		List<String> lst = mapper.getInstProcessIdByInstanceId(instanceId);
		if(lst != null && lst.size()>0)
			return lst.get(0);
		return null;
	}
	
	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public DefWorkItem getUndoWorkItem(String instanceId,String userId) throws Exception
	{	
		DefWorkItem wi = null;
		java.util.List lst = mapper.getUndoWorkItem(instanceId, userId);
		java.util.Iterator it = lst.iterator();
		if(it.hasNext())
		{
			return (DefWorkItem)it.next();
		}
		return null;
	}

	//需要区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public DefWorkItem saveStartWorkItem(String processId,String processName,String userId,String title,String url,
			String instanceId,java.util.HashMap hmap,String flag2,String testMode) throws Exception
	{	
		DefWorkItem wi = null;
		String xmlData = "";
		if(hmap != null)
		{
			xmlData = xmlData + "<?xml version=\"1.0\" encoding=\"gb2312\"?><data>";
			java.util.Set keyset = hmap.keySet();
			for(java.util.Iterator it=keyset.iterator();it.hasNext();)
			{
				Object key = it.next();
				Object value = hmap.get(key);
				xmlData = xmlData + "<"+key+"><![CDATA["+value+"]]></"+key+">";
			}
			xmlData = xmlData + "</data>";
		}
		//instanceId 可以区分分系统标志flag和测试标志testMode
		java.util.List lst = mapper.getWorkItemByInstanceIdAndFlag(instanceId, "undo");
		java.util.Iterator it = lst.iterator();
		boolean isnew = true;
		if(it.hasNext())
		{
			wi = (DefWorkItem)it.next();
			isnew = false;
		}
		else
		{
			wi = new DefWorkItem();
			String id = dpdutil.createId("workitem");
			String instProcessId = dpdutil.createId("process");
			String instActivityId = dpdutil.createId("activity");
			wi.setId(id);
			wi.setInstProcessId(instProcessId);
			wi.setInstActivityId(instActivityId);
			wi.setProcessId(processId);
			wi.setProcessName(processName);
			wi.setStatusId("Start");
			wi.setStatusName("开始");
			wi.setStartUserId(userId);
			wi.setUserId(userId);
			wi.setTitle(title);
			wi.setType(processId);
			wi.setInstanceId(instanceId);
			wi.setFinishTime(new java.sql.Timestamp(new Date().getTime()));
			wi.setUrl(url);
			wi.setFlag("undo");
			wi.setFlag2(flag2);
			wi.setTestMode(testMode);
		}
		wi.setXmlData(xmlData);
		if(isnew)
			mapper.saveWorkitem(wi);
		return wi;
	}
	
	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public void finishStartWorkItem(String instanceId) throws Exception
	{	
		DefWorkItem wi = null;
		java.util.List lst = mapper.getWorkItemByInstanceIdAndStatusIdAndFlag(instanceId, "Start","undo");
		java.util.Iterator it = lst.iterator();
		boolean isnew = true;
		if(it.hasNext())
		{
			wi = (DefWorkItem)it.next();
			wi.setFinishTime(new java.sql.Timestamp(new Date().getTime()));
			wi.setFlag("done");
			mapper.updateWorkitem(wi);
		}
	}

	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public void deleteUndoWorkItem(String instanceId) throws Exception
	{
		java.util.List lst = mapper.getWorkItemByInstanceIdAndFlag(instanceId, "undo");
		java.util.Iterator it = lst.iterator();
		if(it.hasNext())
		{
			DefWorkItem dwi = (DefWorkItem)it.next();
			mapper.deleteWorkitem(dwi.getId());
		}
	}

	//需要区分分系统标志flag和测试标志testMode
	private StatusMsg beforeInvokeApp(String clz,String command,String defProcessId,
		String type,String instanceId,String fromUserId,java.util.List toUserIdLst,
		String title,String url,String fromStatusId,String toStatusId,String flag2,String testMode) throws Exception
	{
		try
		{
			InvokeApp ia = (InvokeApp)(Class.forName(clz).newInstance());
			StatusMsg rtn = ia.beforeInvoke(this,command,defProcessId,type,instanceId,fromUserId,toUserIdLst,
				title,url,fromStatusId,toStatusId,flag2,testMode);
			return rtn;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//需要区分分系统标志flag和测试标志testMode
	private StatusMsg afterInvokeApp(String clz,String command,String defProcessId,
			String type,String instanceId,String instProcessId,String instActivityId,
			java.util.List<DefWorkItem> dwiList,String fromUserId,List userIdLst,
			String title,String url,String fromStatusId,String toStatusId,String flag2,String testMode) throws Exception
		{
			try
			{
				InvokeApp ia = (InvokeApp)(Class.forName(clz).newInstance());
				StatusMsg rtn = ia.afterInvoke(this,command,defProcessId,type,
						instanceId,instProcessId,instActivityId,dwiList,
						fromUserId,userIdLst,title,url,fromStatusId,toStatusId,flag2,testMode);
				return rtn;
			}
			catch(Exception e)
			{
				throw e;
			}
		}

	//instanceId 可以区分分系统标志flag和测试标志testMode
	/**
	* 统一修改关联业务主键instanceid的工作流待办件中业务数据
	*/
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED)
	public void saveWorkitemXmlData(String instanceId,java.util.HashMap hmap)
	{
		try
		{
			java.util.List lst = mapper.getWorkItemListByInstanceId(instanceId);
			for(java.util.Iterator it=lst.iterator();it.hasNext();)
			{
				DefWorkItem dwi = (DefWorkItem)it.next();
				String id = dwi.getId();
				String xmldata = dwi.getXmlData();
				//xmldata like as "<?xml version="1.0" encoding="gb2312"?><data><amount><![CDATA[20.0]]></amount><no><![CDATA[VOUNAVIT201000002]]></no></data>"
				String cleanxmldata1 = "<?xml version=\"1.0\" encoding=\"gb2312\"?><data>";
				String cleanxmldata2 = "</data>";
				String cleanxmldata = xmldata.substring(cleanxmldata1.length());
				cleanxmldata = cleanxmldata.substring(0,cleanxmldata.indexOf(cleanxmldata2));
				java.util.Collection keycol = hmap.keySet();
				for(java.util.Iterator it2=keycol.iterator();it2.hasNext();)
				{
					Object name = it2.next();
					Object value = hmap.get(name);
					String str1 = "<"+name+">";
					String str2 = "</"+name+">";
					int pos1 = cleanxmldata.indexOf(str1);
					if(pos1<0)
					{
						cleanxmldata = cleanxmldata + str1 + value + str2;
					}
					else
					{
						int pos2 = cleanxmldata.indexOf(str2);
						String xmldata2 = cleanxmldata.substring(0,pos1+str1.length());
						String xmldata3 = cleanxmldata.substring(pos2);
						cleanxmldata = xmldata2+"<![CDATA["+value+"]]>"+xmldata3;
					}
				}
				xmldata = cleanxmldata1+cleanxmldata+cleanxmldata2;
				dwi.setXmlData(xmldata);
				mapper.updateWorkitem(dwi);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//instWorkitemId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public boolean isValidWorkitem(String userId,String instProcessId,String instActivityId,
			String instWorkitemId)
	{
		java.util.List lst = null;
		lst = mapper.getWorkItemListByUidPidAidWid(userId, instProcessId, instActivityId, instWorkitemId);
		if(lst.size()>0)
			return true;
		else
			return false;
	}
	
	//instWorkitemId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public boolean isValidWorkitem(String instProcessId,String instActivityId,
			String instWorkitemId)
	{
		java.util.List lst = null;
		lst = mapper.getWorkItemListByPidAidWid(instProcessId, instActivityId, instWorkitemId);
		if(lst.size()>0)
			return true;
		else
			return false;
	}
	
	//instanceId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public boolean hasStartActivity(String instanceId)
	{
		java.util.List lst = null;
		lst = mapper.getWorkItemByInstanceIdAndStatusId(instanceId, "Start");
		if(lst.size()>0)
			return true;
		else
			return false;
	}
	
	//instanceIdList 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public byte[] getUiImage(String processId,List<String> instanceIdList,int width,int height) throws Exception
	{
		//获取最新的工作流版本
		processId = Util.getLastWorkflow(processId);
		
		java.util.List<DefWorkItem> dwiList = new ArrayList<DefWorkItem>();
		for(String instanceId:instanceIdList)
		{
			java.util.List<DefWorkItem> dwiList2 = getWorkItemList(instanceId);
			dwiList.addAll(dwiList2);
		}

		//按时间升序排列
		DefWorkItem[] dwiDim = new DefWorkItem[dwiList.size()];
		int pos = 0;
		for(DefWorkItem dwi:dwiList)
		{
			dwiDim[pos] = dwi;
			pos++;
		}
		for(int i=0;i<dwiDim.length;i++)
		{
			for(int j=i;j<dwiDim.length;j++)
			{
				if(dwiDim[i].getCreateTime().getTime()>dwiDim[j].getCreateTime().getTime())
				{
					DefWorkItem swap = dwiDim[j];
					dwiDim[j] = dwiDim[i];
					dwiDim[i] = swap;
				}
			}
		}
		java.util.List<DefWorkItem> dwiList2 = new ArrayList<DefWorkItem>();
		for(int i=0;i<dwiDim.length;i++)
		{
			dwiList2.add(dwiDim[i]);
		}
		
		return uiimg.genImage(processId, dwiList2,width,height);
	}
	
	//instProcessId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public byte[] getUiImageByInstProcessId(String processId,String instProcessId,int width,int height) throws Exception
	{		
		java.util.List<DefWorkItem> dwiList = getWorkItemListByInstProcessId(instProcessId);
		//按时间升序排列
		DefWorkItem[] dwiDim = new DefWorkItem[dwiList.size()];
		int pos = 0;
		for(DefWorkItem dwi:dwiList)
		{
			dwiDim[pos] = dwi;
			pos++;
		}
		for(int i=0;i<dwiDim.length;i++)
		{
			for(int j=i;j<dwiDim.length;j++)
			{
				if(dwiDim[i].getCreateTime().getTime()>dwiDim[j].getCreateTime().getTime())
				{
					DefWorkItem swap = dwiDim[j];
					dwiDim[j] = dwiDim[i];
					dwiDim[i] = swap;
				}
			}
		}
		java.util.List<DefWorkItem> dwiList2 = new ArrayList<DefWorkItem>();
		for(int i=0;i<dwiDim.length;i++)
		{
			dwiList2.add(dwiDim[i]);
			processId = dwiDim[i].getProcessId();
		}
		return uiimg.genImage(processId, dwiList2,width,height);
	}
	
	//instWorkitemId 可以区分分系统标志flag和测试标志testMode
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED)
	public String getStartUserId(String instWorkitemId)
	{
		DefWorkItem dwi = mapper.getWorkitemById(instWorkitemId);
		if(dwi != null)
			return dwi.getStartUserId();
		else
			return null;
	}
	
	private void fillData(DefWorkItem dwi)
	{
		String flag = dwi.getFlag();
		if("undo".equals(flag))
			dwi.setFlagName("草稿");
		if(dwi.getFinishTime() != null)
			dwi.setFlagName("完成");
		if(dwi.getFinishTime() == null)
			dwi.setFlagName("审批中");
		if("pass".equals(flag))
			dwi.setFlagName("通过");
		String userId = dwi.getUserId();
		String userName = dpdutil.getUserName(userId);
		dwi.setUserIdName(userName);
		String startUserId = dwi.getStartUserId();
		String startUserName = dpdutil.getUserName(startUserId);
		dwi.setStartUserIdName(startUserName);
	}
	
	private void fillData(List<DefWorkItem> dwiList)
	{
		for(DefWorkItem dwi:dwiList)
			fillData(dwi);
	}

}