package com.gf.statusflow.def;

import java.util.ArrayList;
import java.util.List;

public class UiProcess {
	private String id = null;
	private String name = null;
	private String description = null;
	private List<UiNode> nodeList = new ArrayList<UiNode>();
	
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
	public List<UiNode> getNodeList() {
		return nodeList;
	}
	public void setNodeList(List<UiNode> nodeList) {
		this.nodeList = nodeList;
	}
}
