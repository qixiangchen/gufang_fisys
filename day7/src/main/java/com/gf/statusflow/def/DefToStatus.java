package com.gf.statusflow.def;

public class DefToStatus
{
//	private String batchId = null;
	private String id = null;
	private String name = null;
	private String condition = null;
	private String invoke = null;
	private String invokeCommand = null;
	private String type = null;

//	public void setBatchId(String batchId)
//	{
//		this.batchId = batchId;
//	}
//
//	public String getBatchId()
//	{
//		return batchId;
//	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setInvoke(String invoke)
	{
		this.invoke = invoke;
	}

	public String getInvoke()
	{
		return invoke;
	}

	public void setInvokeCommand(String invokeCommand)
	{
		this.invokeCommand = invokeCommand;
	}

	public String getInvokeCommand()
	{
		return invokeCommand;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString()
	{
		return "DefToStatus[id="+id+",name="+name+",condition="+condition+",invoke="+invoke+",invokeCommand="+invokeCommand+"]";
	}
}