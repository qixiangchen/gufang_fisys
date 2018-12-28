package com.gf.statusflow;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultUser;

public interface IOrgModel
{
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
	public List<DefaultUser> getUserByOrgId(String orgId);
	public DefaultUser getManagerById(String userId);
	public List<DefaultUser> getUserByManagerId(String managerId);
	public DefaultUser checkLogin(String loginId,String password);
	public DefaultUser getUserByLoginId(String loginId);
	
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
}