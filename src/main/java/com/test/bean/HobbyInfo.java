package com.test.bean;

import java.io.Serializable;

public class HobbyInfo implements Serializable{
	private Integer id = null;
	private String name = null;
	private String checked = "";
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}

}
