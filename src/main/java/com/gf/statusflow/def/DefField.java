package com.gf.statusflow.def;

public class DefField {
	private String id = null;
	private String privilege = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	public String toString()
	{
		return "DefField[id="+id+",privilege="+privilege+"]";
	}
}
