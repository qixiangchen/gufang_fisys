package com.gf.statusflow.def;

import java.util.ArrayList;
import java.util.List;

public class UiNode {
	private String id = null;
	private String name = null;
	private String userName = null;
	private String x = null;
	private String y = null;
	private String nameX = null;
	private String nameY = null;
	private String textX = null;
	private String textY = null;
	private List<String> toNodeList = new ArrayList<String>();
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getNameX() {
		return nameX;
	}
	public void setNameX(String nameX) {
		this.nameX = nameX;
	}
	public String getNameY() {
		return nameY;
	}
	public void setNameY(String nameY) {
		this.nameY = nameY;
	}
	public String getTextX() {
		return textX;
	}
	public void setTextX(String textX) {
		this.textX = textX;
	}
	public String getTextY() {
		return textY;
	}
	public void setTextY(String textY) {
		this.textY = textY;
	}
	public List<String> getToNodeList() {
		return toNodeList;
	}
	public void setToNodeList(List<String> toNodeList) {
		this.toNodeList = toNodeList;
	}

}
