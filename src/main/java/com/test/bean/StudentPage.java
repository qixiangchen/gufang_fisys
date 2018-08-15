package com.test.bean;

import java.io.Serializable;
import java.util.List;

public class StudentPage implements Serializable{
	private List<StudentInfo> list = null;
	private Integer total = null;
	public List<StudentInfo> getList() {
		return list;
	}
	public void setList(List<StudentInfo> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
