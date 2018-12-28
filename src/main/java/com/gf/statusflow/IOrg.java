package com.gf.statusflow;

import java.util.List;

public interface IOrg
{
	public void setId(String id);
	public String getId();
	public void setName(String name);
	public String getName();
	public void setParentId(String parentId);
	public String getParentId();
	public void setOrgPath(String orgPath);
	public String getOrgPath();
	public void setFullName(String fullName);
	public String getFullName();
	public void setSeqno(Integer seqno);
	public Integer getSeqno();
	public void setFlag(String flag);
	public String getFlag();
}