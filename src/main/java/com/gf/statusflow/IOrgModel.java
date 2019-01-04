package com.gf.statusflow;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gf.model.FunctionInfo;
import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;
import com.gf.statusflow.def.Perm2RoleInfo;
import com.gf.statusflow.def.PermissionInfo;

public interface IOrgModel
{
	public String I_SYSADMIN = "sysadmin";
	
	public void initDb();
	/**
	 * 部门相关接口
	 * @return
	 */
	public List<DefaultOrg> getAllOrg();
	public DefaultOrg getRootOrg();
	public void saveOrg(DefaultOrg org);
	public void deleteOrg(String id);
	public void updateOrg(DefaultOrg org);
	public List<DefaultOrg> getOrgList(String id);
	public DefaultOrg getOrgById(String orgId);
	
	/**
	 * 用户相关接口
	 * @param userId
	 * @return
	 */
	public void saveUser(DefaultUser user);
	public void updateUser(DefaultUser user);
	public void deleteUser(String id);
	public List<DefaultUser> findUserByName(String qname);
	public List<DefaultUser> getUserByOrgId(String orgId);
	public DefaultUser getManagerById(String userId);
	public List<DefaultUser> getUserByManagerId(String managerId);
	public DefaultUser checkLogin(String loginId,String password);
	public DefaultUser getUserByLoginId(String loginId);
	
	/**
	 * 角色相关Mybatis方法
	 */
	public void saveRole(DefaultRole role);
	public void updateRole(DefaultRole role);
	public void deleteRole(String id);
	public List<DefaultRole> getAllRole();
	public DefaultRole getRoleById(String id);
	
	/**
	 * 部门与角色，用户与角色相关SQ
	 */
	public void saveOrgUserRole(DefaultOrgUserRole our);
	public void deleteOrgUserRoleByEntityId(String entityId);
	public List<DefaultOrgUserRole> getOrgUserRoleByEntityId(String entityId);
	public List<Map> getUserRoleList(String userId);
	public void deleteOrgUserRoleById(String id);
	public List<DefaultOrgUserRole> getOrgUserRoleByRoleIdEntityId(String roleId,
			String entityId);
	
	/**
	 * Shiro权限相关Mybatis方法
	 */
	public void savePermission(PermissionInfo permission);
	public void updatePermission(PermissionInfo permission);
	public void deletePermById(String id);
	public void deletePermByModule(String id);
	public List<PermissionInfo> getPermission();
	
	/**
	 * 角色与权限中间表Mybatis方法
	 */
	public void savePerm2Role(Perm2RoleInfo p2r);
	public void deletePerm2RoleById(String id);
	public void deletePerm2RoleByRoleId(String roleId);
	public void deletePerm2RoleByPermId(String permId);
	public List<String> getPermByRoleId(String roleId);
	
	/**
	 * 工作流引擎中调用组织机构的方法
	 * @param userId
	 * @return
	 */
	public IUser getUserById(String userId);
	public IUser getUserByLoginId(String flag,String loginId);
	public List<IOrg> getUserListByOrgId(String orgId,String flag);
	public IRole getRoleByNameFlag(String name,String flag);
	public List<IOrgUserRole> getOrgUserRoleListByRoleId(String roleId);
	
	/**
	 * 功能管理模块
	 * @return
	 */
	public FunctionInfo getRootFunc();
	/**
	 * 根据功能模块ID获取此功能下一级模块列表
	 * @param id
	 * @return
	 */
	public List<FunctionInfo> getChildFunc(String id);
	/**
	 * 功能模块保存方法
	 * @param fi
	 */
	public void saveFunc(FunctionInfo fi);
	/**
	 * 功能模块更新方法
	 * @param fi
	 */
	public void updateFunc(FunctionInfo fi);
	/**
	 * 初始化系统功能模块，包括(系统管理，用户管理，部门管理等模块)
	 * @return
	 */
	public FunctionInfo initFunc();
	/**
	 * 根据模块ID删除此模块，系统功能模块不可删除
	 * @param id
	 */
	public void deleteFunc(String id);
	public FunctionInfo getFuncById(String id);
}