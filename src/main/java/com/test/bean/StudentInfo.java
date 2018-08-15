package com.test.bean;

import java.io.Serializable;

public class StudentInfo implements Serializable{
	private Integer id = null;
	private String name = null;
	private String grade = null;
	
	private String hname = null;//不同兴趣以逗号分隔
	private String hid = null;//不同兴趣ID以逗号分隔
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}	
}
