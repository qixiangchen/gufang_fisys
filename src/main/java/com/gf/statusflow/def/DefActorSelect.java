package com.gf.statusflow.def;

public class DefActorSelect
{
	private String id = null;
	private String type = null;
	private String loginId = null;
	private String switchValue = null;

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getSwitchValue() {
		return switchValue;
	}

	public void setSwitchValue(String switchValue) {
		this.switchValue = switchValue;
	}

	public String toString()
	{
		return "DefActorSelect[id="+id+",loginId="+loginId+",type="+type+",switchValue="+switchValue+"]";
	}
}