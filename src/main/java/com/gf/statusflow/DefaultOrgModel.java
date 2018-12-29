package com.gf.statusflow;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;

@Component
public class DefaultOrgModel implements IOrgModel{
	private Logger log = LoggerFactory.getLogger(DefaultOrgModel.class);
	
	@Autowired
	private WorkflowMapper mapper;
	
	@Override
	public List<DefaultOrg> getAllOrg()
	{
		return mapper.getAllOrg();
	}
	@Override
	public DefaultOrg getRootOrg() {
		try
		{
			return mapper.getRootOrg();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void saveOrg(DefaultOrg org) {
		log.debug("saveOrg org="+org.getId()+","+org.getName()+","+org.getParentId());
		try
		{
			IOrg parentOrg = this.getOrgById(org.getParentId());
			log.debug("saveOrg parentOrg="+parentOrg);
			String id = UUID.create("org");
			org.setId(id);
			String orgPath = parentOrg.getOrgPath();
			orgPath = orgPath + "/"+id;
			org.setOrgPath(orgPath);
			String fullName = parentOrg.getFullName();
			fullName = fullName + "/" + org.getName();
			org.setFullName(fullName);
			mapper.saveOrg(org);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	public void updateOrg(DefaultOrg org) {
		try
		{
			mapper.updateOrg(org);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	@Override
	public void deleteOrg(String id) {
		try
		{
			mapper.deleteOrg(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	public List<DefaultOrg> getOrgList(String id) {
		try
		{
			return mapper.getOrgList(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	

	@Override
	public DefaultOrg getOrgById(String orgId) {
		try
		{
			return mapper.getOrgById(orgId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	

	@Override
	public void saveUser(DefaultUser user) {
		try
		{
			mapper.saveUser(user);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	public void updateUser(DefaultUser user) {
		try
		{
			mapper.updateUser(user);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	public void deleteUser(String id) {
		try
		{
			mapper.deleteUser(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	@Override
	public List<DefaultUser> findUserByName(String qname) {
		try
		{
			return mapper.findUserByName(qname);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<DefaultUser> getUserByOrgId(String orgId) {
		try
		{
			return mapper.getUserByOrgId(orgId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public DefaultUser getManagerById(String userId) {
		try
		{
			return mapper.getManagerById(userId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<DefaultUser> getUserByManagerId(String managerId) {
		try
		{
			return mapper.getUserByManagerId(managerId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public DefaultUser checkLogin(String loginId, String password) {
		try
		{
			return mapper.checkLogin(loginId,password);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public DefaultUser getUserByLoginId(String loginId)
	{
		try
		{
			return mapper.getUserByLoginId(loginId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 工作流引擎中调用组织机构的方法
	 * @param userId
	 * @return
	 */
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
	
	/**
	 * 角色相关Mybatis方法
	 */
	@Override
	public void saveRole(DefaultRole role) {
		try
		{
			mapper.saveRole(role);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	@Override
	public void updateRole(DefaultRole role) {
		try
		{
			mapper.updateRole(role);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	@Override
	public void deleteRole(String id) {
		try
		{
			mapper.deleteRole(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	@Override
	public List<DefaultRole> getAllRole() {
		try
		{
			return mapper.getAllRole();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	@Override
	public DefaultRole getRoleById(String id) {
		try
		{
			return mapper.getRoleById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 部门与角色，用户与角色相关SQ
	 */
	@Override
	public void saveOrgUserRole(DefaultOrgUserRole our) {
		try
		{
			mapper.saveOrgUserRole(our);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	@Override
	public void deleteOrgUserRoleByEntityId(String entityId) {
		try
		{
			mapper.deleteOrgUserRoleByEntityId(entityId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	public void deleteOrgUserRoleById(String id)
	{
		try
		{
			mapper.deleteOrgUserRoleById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public List<DefaultOrgUserRole> getOrgUserRoleByRoleIdEntityId(String roleId,
			String entityId)
	{
		try
		{
			return mapper.getOrgUserRoleByRoleIdEntityId(roleId,entityId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	@Override
	public List<DefaultOrgUserRole> getOrgUserRoleByEntityId(String entityId) {
		try
		{
			return mapper.getOrgUserRoleByEntityId(entityId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	public List<Map> getUserRoleList(String userId)
	{
		try
		{
			return mapper.getUserRoleList(userId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
}
