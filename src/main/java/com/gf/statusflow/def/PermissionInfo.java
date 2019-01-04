package com.gf.statusflow.def;

import org.apache.shiro.authz.annotation.RequiresPermissions;

public class PermissionInfo {
	private String id = null;
	private String moduleName = null;//组织机构部门
	private String name = null;//权限名称
	private String permission = null;//对应到Controller上的注解名称 @RequiresPermissions("url:orgsave.action")
	private String roleName = null;
	private String roleId = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}	
}
