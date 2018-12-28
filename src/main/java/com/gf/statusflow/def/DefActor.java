package com.gf.statusflow.def;

import java.util.ArrayList;

public class DefActor
{
	private String id = null;
	private String type = null;
	private String loginId = null;
	private String autoSelect = "false";
	private String condition = null;
	private java.util.List<DefActorSelect> selectLst = new java.util.ArrayList<DefActorSelect>();

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

	public String getAutoSelect() {
		return autoSelect;
	}

	public void setAutoSelect(String autoSelect) {
		this.autoSelect = autoSelect;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public java.util.List<DefActorSelect> getSelectLst() {
		return selectLst;
	}

	public void setSelectLst(java.util.List<DefActorSelect> selectLst) {
		this.selectLst = selectLst;
	}
	
	public void addSelectLst(DefActorSelect sel) {
		if(selectLst == null)
			selectLst = new ArrayList<DefActorSelect>();
		selectLst.add(sel);
	}

	public String toString()
	{
		return "DefActor[id="+id+",loginId="+loginId+",type="+type+",autoSelect="+autoSelect+",condition="+condition+",selectLst="+selectLst+"]";
	}
}