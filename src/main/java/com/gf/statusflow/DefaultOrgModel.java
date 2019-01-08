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
import com.gf.statusflow.def.Menu2RoleInfo;
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
			sysUser.setId(I_SYSADMIN_ID);
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
	
	@Override
	public DefaultUser getUserById(String userId) {
		try
		{
			return mapper.getUserById(userId);
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
	
	public void deletePermByFuncId(String module)
	{
		try
		{
			mapper.deletePermByFuncId(module);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public List<PermissionInfo> getPermissionByFuncId(String id)
	{
		try
		{
			return mapper.getPermissionByFuncId(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public List<PermissionInfo> getPermission(String permId,
			String funcId,String roleId)
	{
		try
		{
			return mapper.getPermission(permId,funcId,roleId);
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
	public List<FunctionInfo> getAllFunc()
	{
		try
		{
			return mapper.getAllFunc();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
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
	
	public List<FunctionInfo> getListByPath(String path)
	{
		try
		{
			return mapper.getListByPath(path);
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
		String rootId = "func000000000000";
		FunctionInfo root = getFuncById(rootId);
		if(root == null)
		{
			root = new FunctionInfo();
			root.setId(rootId);
			root.setName("功能模块");
			root.setParentId(null);
			root.setPath("/func000000000000");
			root.setFullName("/功能模块");
			this.saveFunc(root);
		}
		String sysId = "func000000001000";
		FunctionInfo sys = getFuncById(sysId);
		if(sys == null)
		{
			sys = new FunctionInfo();
			sys.setId(sysId);
			sys.setName("系统管理");
			sys.setIcon("pic_1");
			sys.setParentId(root.getId());
			sys.setPriority(1);
			sys.setPath("/func000000000000/func000000001000");
			sys.setFullName("/功能模块/系统管理");
			this.saveFunc(sys);
		}
		String functionId = "func000000001100";
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
			funmgr.setPath("/func000000000000/func000000001000/func000000001100");
			funmgr.setFullName("/功能模块/系统管理/功能管理");
			this.saveFunc(funmgr);
		}
		String orgmodelId = "func000000001200";
		FunctionInfo orgmodel = getFuncById(orgmodelId);
		if(orgmodel == null)
		{
			orgmodel = new FunctionInfo();
			orgmodel.setId(orgmodelId);
			orgmodel.setName("组织机构");
			orgmodel.setIcon("pic_3");
			orgmodel.setParentId(sys.getId());
			orgmodel.setPriority(2);
			orgmodel.setPath("/func000000000000/func000000001000/func000000001200");
			orgmodel.setFullName("/功能模块/系统管理/组织机构");
			this.saveFunc(orgmodel);
		}
		String orgId = "func000000001210";
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
			org.setPath("/func000000000000/func000000001000/func000000001200/func000000001210");
			org.setFullName("/功能模块/系统管理/组织机构/部门管理");
			this.saveFunc(org);
		}
		String userId = "func000000001220";
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
			user.setPath("/func000000000000/func000000001000/func000000001200/func000000001220");
			user.setFullName("/功能模块/系统管理/组织机构/用户管理");
			this.saveFunc(user);
		}
		String roleId = "func000000001230";
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
			role.setPath("/func000000000000/func000000001000/func000000001200/func000000001230");
			role.setFullName("/功能模块/系统管理/组织机构/角色管理");
			this.saveFunc(role);
		}
		String userroleId = "func000000001240";
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
			userrole.setPath("/func000000000000/func000000001000/func000000001200/func000000001240");
			userrole.setFullName("/功能模块/系统管理/组织机构/角色分配");
			this.saveFunc(userrole);
		}
		String moduleId = "func000000001300";
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
			module.setPath("/func000000000000/func000000001000/func000000001300");
			module.setFullName("/功能模块/系统管理/模块权限");
			this.saveFunc(module);
		}
		String moduleaclId = "func000000001400";
		FunctionInfo moduleacl = getFuncById(moduleaclId);
		if(moduleacl == null)
		{
			moduleacl = new FunctionInfo();
			moduleacl.setId(moduleaclId);
			moduleacl.setName("模块角色授权");
			moduleacl.setIcon("pic_12");
			moduleacl.setParentId(sys.getId());
			moduleacl.setUrl("/moduleacl.action");
			moduleacl.setPriority(4);
			moduleacl.setPath("/func000000000000/func000000001000/func000000001400");
			moduleacl.setFullName("/功能模块/系统管理/模块角色授权");
			this.saveFunc(moduleacl);
		}
		String menuaclId = "func000000001500";
		FunctionInfo menuacl = getFuncById(menuaclId);
		if(menuacl == null)
		{
			menuacl = new FunctionInfo();
			menuacl.setId(menuaclId);
			menuacl.setName("菜单授权");
			menuacl.setIcon("pic_18");
			menuacl.setParentId(sys.getId());
			menuacl.setUrl("/menuacl.action");
			menuacl.setPriority(4);
			menuacl.setPath("/func000000000000/func000000001000/func000000001500");
			menuacl.setFullName("/功能模块/系统管理/菜单授权");
			this.saveFunc(menuacl);
		}
		String fiId = "func000000002000";
		FunctionInfo fi = getFuncById(fiId);
		if(fi == null)
		{
			fi = new FunctionInfo();
			fi.setId(fiId);
			fi.setName("财务管理");
			fi.setIcon("pic_20");
			fi.setParentId(root.getId());
			fi.setPriority(1);
			fi.setPath("/func000000000000/func000000002000");
			fi.setFullName("/功能模块/财务管理");
			this.saveFunc(fi);
		}
		String workitemId = "func000000002100";
		FunctionInfo workitem = getFuncById(workitemId);
		if(workitem == null)
		{
			workitem = new FunctionInfo();
			workitem.setId(workitemId);
			workitem.setName("代办工作");
			workitem.setIcon("pic_20");
			workitem.setParentId(fi.getId());
			workitem.setPriority(1);
			workitem.setUrl("/workitem.action");
			workitem.setPath("/func000000000000/func000000002000/func000000002100");
			workitem.setFullName("/功能模块/财务管理/财务报销");
			this.saveFunc(workitem);
		}
		String expenseId = "func000000002200";
		FunctionInfo expense = getFuncById(expenseId);
		if(expense == null)
		{
			expense = new FunctionInfo();
			expense.setId(expenseId);
			expense.setName("财务报销");
			expense.setIcon("pic_21");
			expense.setParentId(fi.getId());
			expense.setPriority(2);
			expense.setUrl("/expense.action");
			expense.setPath("/func000000000000/func000000002000/func000000002100");
			expense.setFullName("/功能模块/财务管理/财务报销");
			this.saveFunc(expense);
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
	
	/**
	 * 菜单角色相关Mybatis方法
	 */
	public void saveMenuRole(Menu2RoleInfo m2r)
	{
		try
		{
			mapper.saveMenuRole(m2r);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void updateMenuRole(Menu2RoleInfo m2r)
	{
		try
		{
			mapper.updateMenuRole(m2r);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deleteMenuRoleById(String id)
	{
		try
		{
			mapper.deleteMenuRoleById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deleteMenuRoleByRoleId(String roleId)
	{
		try
		{
			mapper.deleteMenuRoleByRoleId(roleId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public void deleteMenuRoleByFuncId(String funcId)
	{
		try
		{
			mapper.deleteMenuRoleByFuncId(funcId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public Menu2RoleInfo getMenuRoleById(String id)
	{
		try
		{
			return mapper.getMenuRoleById(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public List<Menu2RoleInfo> getMenuRoleByRoleId(String roleId)
	{
		try
		{
			return mapper.getMenuRoleByRoleId(roleId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public List<Menu2RoleInfo> getMenuRoleByFuncId(String funcId)
	{
		try
		{
			return mapper.getMenuRoleByFuncId(funcId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public List<FunctionInfo> getFuncListByUserId(String userId)
	{
		try
		{
			return mapper.getFuncListByUserId(userId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public List<Menu2RoleInfo> getAclMenu(String funcId,String roleId)
	{
		try
		{
			return mapper.getAclMenu(funcId,roleId);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
}
