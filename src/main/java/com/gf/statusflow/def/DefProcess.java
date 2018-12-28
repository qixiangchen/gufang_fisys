package com.gf.statusflow.def;

public class DefProcess
{
	private String id = null;
	private String name = null;
	private String description = null;
	private java.util.List statusLst = new java.util.ArrayList();
	private java.util.HashMap extMap = new java.util.HashMap();
	private String uiwf = null;

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

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public String getUiwf() {
		return uiwf;
	}

	public void setUiwf(String uiwf) {
		this.uiwf = uiwf;
	}

	public void setStatusLst(java.util.List statusLst)
	{
		this.statusLst = statusLst;
		if(statusLst == null)
			statusLst = new java.util.ArrayList();
	}

	public java.util.List getStatusLst()
	{
		return statusLst;
	}

	public void addStatusLst(DefStatus ds)
	{
		if(statusLst == null)
			statusLst = new java.util.ArrayList();
		statusLst.add(ds);
	}

	public void setExtMap(java.util.HashMap extMap)
	{
		this.extMap = extMap;
		if(extMap == null)
			extMap = new java.util.HashMap();
	}

	public java.util.HashMap getExtMap()
	{
		return extMap;
	}

	public void addExtMap(String key,String value)
	{
		if(extMap == null)
			extMap = new java.util.HashMap();
		extMap.put(key,value);
	}

	public DefStatus getStart()
	{
		for(java.util.Iterator it=statusLst.iterator();it.hasNext();)
		{
			DefStatus ds = (DefStatus)it.next();
			if(ds.getIsStart())
				return ds;
		}
		return null;
	}

	public DefStatus getStatus(String statusId)
	{
		for(java.util.Iterator it=statusLst.iterator();it.hasNext();)
		{
			DefStatus ds = (DefStatus)it.next();
			if(ds.getId().equals(statusId))
				return ds;
		}
		return null;
	}

	public String getExtMap(String key)
	{
		return (String)extMap.get(key);
	}

	public String toString()
	{
		return "DefProcess[id="+id+",name="+name+",description="+description+",extMap="+extMap+",statusLst="+statusLst+"]";
	}
}