package com.gf.statusflow.def;

import com.gf.statusflow.IOrg;
import com.gf.statusflow.IRole;

public class DefaultRole implements IRole{
	private String id = null;
	private String name = null;
	private String description = null;
	private String flag = "gf";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
