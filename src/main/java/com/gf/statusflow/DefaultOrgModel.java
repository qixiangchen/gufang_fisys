package com.gf.statusflow;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gf.model.FunctionInfo;
import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;
import com.gf.statusflow.def.Perm2RoleInfo;
import com.gf.statusflow.def.PermissionInfo;

@Component
public class DefaultOrgModel implements IOrgModel{
	private Logger log = LoggerFactory.getLogger(DefaultOrgModel.class);
	@Autowired
	private WorkflowMapper mapper;
	@Value("${orgmodel.sysadmin.pwd}")
	private String sysAdminPwd = null;
	
	public void initDb()
	{
		DefaultUser sysUser = mapper.getUserByLoginId(I_SYSADMIN);
		if(sysUser == null)
		{
			sysUser = new DefaultUser();
			sysUser.setId("1");
			sysUser.setLoginId(I_SYSADMIN);
			sysUser.setPassword(Util.getMD5(sysAdminPwd));
			sysUser.setName("系统管理员");
			mapper.saveUser(sysUser);
		}
		
	}
	
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
	
	/**
	 * Shiro权限相关Mybatis方法
	 */
	public void savePermission(PermissionInfo permission)
	{
		try
		{
			mapper.savePermission(permission);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void updatePermission(PermissionInfo permission)
	{
		try
		{
			mapper.updatePermission(permission);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deletePermById(String id)
	{
		try
		{
			mapper.deletePermById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deletePermByModule(String module)
	{
		try
		{
			mapper.deletePermByModule(module);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public List<PermissionInfo> getPermission()
	{
		try
		{
			return mapper.getPermission();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 角色与权限中间表Mybatis方法
	 */
	public void savePerm2Role(Perm2RoleInfo p2r)
	{
		try
		{
			mapper.savePerm2Role(p2r);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deletePerm2RoleById(String id)
	{
		try
		{
			mapper.deletePerm2RoleById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deletePerm2RoleByPermId(String permId)
	{
		try
		{
			mapper.deletePerm2RoleByPermId(permId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deletePerm2RoleByRoleId(String roleId)
	{
		try
		{
			mapper.deletePerm2RoleByRoleId(roleId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public List<String> getPermByRoleId(String roleId)
	{
		try
		{
			return mapper.getPermByRoleId(roleId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 功能管理模块
	 * @return
	 */
	public FunctionInfo getRootFunc()
	{
		try
		{
			return mapper.getRootFunc();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据功能模块ID获取此功能下一级模块列表
	 * @param id
	 * @return
	 */
	public List<FunctionInfo> getChildFunc(String id)
	{
		try
		{
			return mapper.getChildFunc(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 功能模块保存方法
	 * @param fi
	 */
	public void saveFunc(FunctionInfo fi)
	{
		try
		{
			mapper.saveFunc(fi);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	/**
	 * 功能模块更新方法
	 * @param fi
	 */
	public void updateFunc(FunctionInfo fi)
	{
		try
		{
			mapper.updateFunc(fi);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	/**
	 * 初始化系统功能模块，包括(系统管理，用户管理，部门管理等模块)
	 * @return
	 */
	public FunctionInfo initFunc()
	{
		String rootId = "root";
		FunctionInfo root = getFuncById(rootId);
		if(root == null)
		{
			root = new FunctionInfo();
			root.setId(rootId);
			root.setName("功能模块");
			root.setParentId(null);
			this.saveFunc(root);
		}
		String sysId = "system";
		FunctionInfo sys = getFuncById(sysId);
		if(sys == null)
		{
			sys = new FunctionInfo();
			sys.setId(sysId);
			sys.setName("系统管理");
			sys.setIcon("pic_1");
			sys.setParentId(root.getId());
			sys.setPriority(1);
			this.saveFunc(sys);
		}
		String functionId = "function";
		FunctionInfo funmgr = getFuncById(functionId);
		if(funmgr == null)
		{
			funmgr = new FunctionInfo();
			funmgr.setId(functionId);
			funmgr.setName("功能管理");
			funmgr.setIcon("pic_2");
			funmgr.setParentId(sys.getId());
			funmgr.setUrl("/function.action");
			funmgr.setPriority(1);
			this.saveFunc(funmgr);
		}
		String orgmodelId = "orgmodel";
		FunctionInfo orgmodel = getFuncById(orgmodelId);
		if(orgmodel == null)
		{
			orgmodel = new FunctionInfo();
			orgmodel.setId(orgmodelId);
			orgmodel.setName("组织机构");
			orgmodel.setIcon("pic_3");
			orgmodel.setParentId(sys.getId());
			orgmodel.setPriority(2);
			this.saveFunc(orgmodel);
		}
		String orgId = "orgmodel_org";
		FunctionInfo org = getFuncById(orgId);
		if(org == null)
		{
			org = new FunctionInfo();
			org.setId(orgId);
			org.setName("部门管理");
			org.setIcon("pic_4");
			org.setParentId(orgmodel.getId());
			org.setUrl("/org.action");
			org.setPriority(2);
			this.saveFunc(org);
		}
		String userId = "orgmodel_user";
		FunctionInfo user = getFuncById(userId);
		if(user == null)
		{
			user = new FunctionInfo();
			user.setId(userId);
			user.setName("用户管理");
			user.setIcon("pic_5");
			user.setParentId(orgmodel.getId());
			user.setUrl("/user.action");
			user.setPriority(2);
			this.saveFunc(user);
		}
		String roleId = "orgmodel_role";
		FunctionInfo role = getFuncById(roleId);
		if(role == null)
		{
			role = new FunctionInfo();
			role.setId(roleId);
			role.setName("角色管理");
			role.setIcon("pic_5");
			role.setParentId(orgmodel.getId());
			role.setUrl("/role.action");
			role.setPriority(2);
			this.saveFunc(role);
		}
		String userroleId = "orgmodel_userrole";
		FunctionInfo userrole = getFuncById(userroleId);
		if(userrole == null)
		{
			userrole = new FunctionInfo();
			userrole.setId(userroleId);
			userrole.setName("角色分配");
			userrole.setIcon("pic_6");
			userrole.setParentId(orgmodel.getId());
			userrole.setUrl("/roleuser.action");
			userrole.setPriority(2);
			this.saveFunc(userrole);
		}
		String moduleId = "module";
		FunctionInfo module = getFuncById(moduleId);
		if(module == null)
		{
			module = new FunctionInfo();
			module.setId(moduleId);
			module.setName("模块权限");
			module.setIcon("pic_11");
			module.setParentId(sys.getId());
			module.setUrl("/module.action");
			module.setPriority(3);
			this.saveFunc(module);
		}
		return root;
	}
	/**
	 * 根据模块ID删除此模块，系统功能模块不可删除
	 * @param id
	 */
	public void deleteFunc(String id)
	{
		try
		{
			mapper.deleteFunc(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public FunctionInfo getFuncById(String id)
	{
		try
		{
			return mapper.getFuncById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
}
