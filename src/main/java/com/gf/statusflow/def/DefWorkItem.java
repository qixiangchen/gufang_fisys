package com.gf.statusflow.def;

public class DefWorkItem implements java.io.Serializable
{
	private String id = null;
	private String startUserId = null;
	private String userId = null;
	private java.sql.Timestamp createTime = new java.sql.Timestamp(new java.util.Date().getTime());
	private java.sql.Timestamp finishTime = null;
	private String processId = null;
	private String processName = null;
	private String title = null;
	private String instanceId = null;
	private String url = null;
	private String instProcessId = null;
	private String instActivityId = null;
	private String statusId = null;
	private String statusName = null;
	private String type = null;
	private String xmlData = null;
	private String flag = null;
	private String flag2 = null;
	private String testMode = null;

	private String startUserIdName;
	private String userIdName;
	private String typeName;
	private String flagName;
	private String reply;

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setStartUserId(String startUserId)
	{
		this.startUserId = startUserId;
	}

	public String getStartUserId()
	{
		return startUserId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setCreateTime(java.sql.Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setFinishTime(java.sql.Timestamp finishTime)
	{
		this.finishTime = finishTime;
	}

	public java.sql.Timestamp getFinishTime()
	{
		return finishTime;
	}

	public void setProcessId(String processId)
	{
		this.processId = processId;
	}

	public String getProcessId()
	{
		return processId;
	}

	public void setProcessName(String processName)
	{
		this.processName = processName;
	}

	public String getProcessName()
	{
		return processName;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setInstanceId(String instanceId)
	{
		this.instanceId = instanceId;
	}

	public String getInstanceId()
	{
		return instanceId;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUrl()
	{
		return url;
	}

	public void setInstProcessId(String instProcessId)
	{
		this.instProcessId = instProcessId;
	}

	public String getInstProcessId()
	{
		return instProcessId;
	}

	public void setInstActivityId(String instActivityId)
	{
		this.instActivityId = instActivityId;
	}

	public String getInstActivityId()
	{
		return instActivityId;
	}

	public void setStatusId(String statusId)
	{
		this.statusId = statusId;
	}

	public String getStatusId()
	{
		return statusId;
	}

	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	public String getStatusName()
	{
		return statusName;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setXmlData(String xmlData)
	{
		this.xmlData = xmlData;
	}

	public String getXmlData()
	{
		return xmlData;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public String getFlag()
	{
		return flag;
	}
	
	public String getFlag2()
	{
		return flag2;
	}
	
	public void setFlag2(String flag2)
	{
		this.flag2 = flag2;
	}
	
	public void setTestMode(String testMode)
	{
		this.testMode = testMode;
	}

	public String getTestMode()
	{
		return testMode;
	}

	public String getStartUserIdName()
	{
		return startUserIdName;
	}

	public void setStartUserIdName(String startUserIdName)
	{
		this.startUserIdName = startUserIdName;
	}

	public String getUserIdName()
	{
		return userIdName;
	}

	public void setUserIdName(String userIdName)
	{
		this.userIdName = userIdName;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	
	public String getFlagName()
	{
		return flagName;
	}

	public void setFlagName(String flagName)
	{
		this.flagName = flagName;
	}
	
	public String getReply()
	{
		return reply;
	}

	public void setReply(String reply)
	{
		this.reply = reply;
	}
	
	public String toString()
	{
		return this.getCreateTime().toString();
	}
}