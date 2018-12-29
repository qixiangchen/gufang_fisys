package com.gf.statusflow;

import java.util.Date;
import java.util.List;


public interface IOrgUserRole extends java.io.Serializable
{
	public String USER = "user";
	public String ORG = "org";
	
	public String getId();
	public void setId(String id);
	public String getEntityId();
	public void setEntityId(String entityId);
	public String getEntityType();
	public void setEntityType(String entityType);//entityType="user",entityType="org"
	public String getRoleId();
	public void setRoleId(String roleId);
	public String getFlag();
	public void setFlag(String flag);
	public String getEntityName();
	public void setEntityName(String entityName);
	public String getRoleName();
	public void setRoleName(String roleName);	
}