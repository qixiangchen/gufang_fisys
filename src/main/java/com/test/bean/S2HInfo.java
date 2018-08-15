package com.test.bean;

import java.io.Serializable;

public class S2HInfo implements Serializable{
	private Integer sid = null;
	private Integer hid = null;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getHid() {
		return hid;
	}
	public void setHid(Integer hid) {
		this.hid = hid;
	}

}
