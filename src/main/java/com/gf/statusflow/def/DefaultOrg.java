package com.gf.statusflow.def;

import com.gf.statusflow.IOrg;

public class DefaultOrg implements IOrg{
	private String id = null;
	private String name = null;
	private String parentId = null;
	private String orgPath = null;// /1/2
	private String fullName = null;// /集团公司/古方红糖厂家/财务部
	private Integer seqno = null;
	private String flag = "gf";
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String getParentId() {
		return parentId;
	}
	@Override
	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}
	@Override
	public String getOrgPath() {
		return orgPath;
	}
	@Override
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Override
	public String getFullName() {
		return fullName;
	}
	@Override
	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}
	@Override
	public Integer getSeqno() {
		return seqno;
	}
	@Override
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public String getFlag() {
		return flag;
	}

}
