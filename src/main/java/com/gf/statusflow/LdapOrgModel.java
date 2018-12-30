package com.gf.statusflow;

import java.util.List;
import java.util.Map;

import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;

public class LdapOrgModel implements IOrgModel{

	@Override
	public List<DefaultOrg> getAllOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultOrg getRootOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrg(DefaultOrg org) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteOrg(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOrg(DefaultOrg org) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DefaultOrg> getOrgList(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultOrg getOrgById(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUser(DefaultUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(DefaultUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DefaultUser> findUserByName(String qname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DefaultUser> getUserByOrgId(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultUser getManagerById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DefaultUser> getUserByManagerId(String managerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultUser checkLogin(String loginId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultUser getUserByLoginId(String loginId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveRole(DefaultRole role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRole(DefaultRole role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRole(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DefaultRole> getAllRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultRole getRoleById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrgUserRole(DefaultOrgUserRole our) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteOrgUserRoleByEntityId(String entityId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DefaultOrgUserRole> getOrgUserRoleByEntityId(String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map> getUserRoleList(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteOrgUserRoleById(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DefaultOrgUserRole> getOrgUserRoleByRoleIdEntityId(String roleId, String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUser getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUser getUserByLoginId(String flag, String loginId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IOrg> getUserListByOrgId(String orgId, String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRole getRoleByNameFlag(String name, String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IOrgUserRole> getOrgUserRoleListByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return null;
	}

}
