package com.gf.statusflow.def;

import com.gf.statusflow.IOrg;
import com.gf.statusflow.IOrgUserRole;

public class DefaultOrgUserRole implements IOrgUserRole{
	private String id = null;
	private String entityId = null;
	private String entityType = USER;
	private String roleId = null;
	private String flag = null;
	//冗余字段
	private String entityName = null;
	private String roleName = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
