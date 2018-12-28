package com.gf.statusflow.def;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
	private String id = null;
	private String text = null;
	private String iconCls = null;
	private String url = null;
	private String state = "closed";
	private Map<String,Object> attributes = new HashMap<String,Object>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(String key,Object value)
	{
		if(attributes == null)
			attributes = new HashMap<String,Object>();
		attributes.put(key, value);
	}
	
}
