package com.gf.statusflow;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;
import com.gf.statusflow.def.Perm2RoleInfo;
import com.gf.statusflow.def.PermissionInfo;
import com.gf.statusflow.def.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class OrgModelCtrl {
	private Logger log = LoggerFactory.getLogger(OrgModelCtrl.class); 
	@Autowired
	private IOrgModel orgmodel;

	//部门页面
	@RequiresPermissions("url:org.action")
	@RequestMapping("/org.action")
	public String org()
	{
		return "/orgmodel/org";
	}
	
	//加载根部门
	//@RequiresPermissions("url:orgroot.action")
	@RequiresPermissions("url:org.action")
	@RequestMapping("/orgroot.action")
	@ResponseBody
	public List<TreeNode> orgroot()
	{
		List<TreeNode> roots = new ArrayList<TreeNode>();
		IOrg org = orgmodel.getRootOrg();
		TreeNode tn = fromOrg(org);
		roots.add(tn);
		return roots;
	}
	
	//加载下一级部门
	//@RequiresPermissions("url:orgchild.action")
	@RequiresPermissions("url:org.action")
	@RequestMapping("/orgchild.action")
	@ResponseBody
	public List<TreeNode> orgchild(String id)
	{
		List<TreeNode> roots = new ArrayList<TreeNode>();
		List<DefaultOrg> orgs = orgmodel.getOrgList(id);
		for(IOrg org:orgs)
		{
			roots.add(fromOrg(org));
		}
		return roots;
	}
	
	//@RequiresPermissions("url:orgsubsave.action")
	@RequiresPermissions("url:org.action")
	@RequestMapping("/orgsubsave.action")
	@ResponseBody
	public Boolean orgsubsave(DefaultOrg org)
	{
		orgmodel.saveOrg(org);
		return true;
	}
	
	//@RequiresPermissions("url:orgsave.action")
	@RequiresPermissions("url:org.action")
	@RequestMapping("/orgsave.action")
	@ResponseBody
	public Boolean orgsave(DefaultOrg org)
	{
		orgmodel.updateOrg(org);
		return true;
	}
	
	//@RequiresPermissions("url:orgload.action")
	@RequiresPermissions("url:org.action")
	@RequestMapping("/orgload.action")
	@ResponseBody
	public List<DefaultOrg> orgload()
	{
		return orgmodel.getAllOrg();
	}
	
	@RequiresPermissions("url:orgdelete.action")
	@RequestMapping("/orgdelete.action")
	@ResponseBody
	public String orgdelete(String id)
	{
		String successRtn = "";
		String failureRtn = "";
		log.debug("orgdelete id="+id);
		String[] dim = id.split(",");
		for(String orgId:dim)
		{
			DefaultOrg org = orgmodel.getOrgById(orgId);
			log.debug("orgdelete orgId="+orgId);
			List<DefaultOrg> child = orgmodel.getOrgList(orgId);
			if(child != null && child.size()>0)
			{
				failureRtn = failureRtn + org.getName();
			}
			else
			{
				orgmodel.deleteOrg(orgId);
				successRtn = successRtn + org.getName();
			}
		}
		String rtn = "删除成功:"+successRtn+"\r\n";
		rtn = rtn + "删除失败:"+failureRtn;
		return rtn;
	}
	
	public TreeNode fromOrg(IOrg org)
	{
		TreeNode tn = new TreeNode();
		tn.setId(org.getId());
		tn.setText(org.getName());
		tn.addAttribute("isload", "false");
		tn.addAttribute("parentId", org.getParentId());
		tn.addAttribute("orgPath", org.getOrgPath());
		tn.addAttribute("fullName", org.getFullName());
		tn.addAttribute("seqno", org.getSeqno());
		tn.addAttribute("flag", org.getFlag());
		return tn;
	}
	
	@RequestMapping("/user.action")
	public String user()
	{
		return "/orgmodel/user";
	}
	
	@RequestMapping("/userload")
	@ResponseBody
	public Map userload(Integer page,Integer rows,String orgId)
	{
		if(page == null)
			page = 1;
		if(rows == null)
			rows = 10;
		Map m = new HashMap();
		if("".equals(Util.fmtStr(orgId)))
		{
			m.put("total", 0);
			m.put("rows", new ArrayList<DefaultUser>());
			return m;
		}
		PageHelper.startPage(page,rows);
		List<DefaultUser> list = orgmodel.getUserByOrgId(orgId);
		PageInfo pi = new PageInfo(list);
		Long total = pi.getTotal();
		List<DefaultUser> users = pi.getList();
		m.put("total", total);
		m.put("rows", users);
		return m;
	}
	
	@RequestMapping("/userquery.action")
	@ResponseBody
	public Map userquery(Integer page,Integer rows,String qname)
	{
		if(page == null)
			page = 1;
		if(rows == null)
			rows = 10;
		Map m = new HashMap();
		PageHelper.startPage(page,rows);
		List<DefaultUser> list = orgmodel.findUserByName(qname);
		PageInfo pi = new PageInfo(list);
		Long total = pi.getTotal();
		List<DefaultUser> users = pi.getList();
		m.put("total", total);
		m.put("rows", users);
		return m;
	}
	
	@RequestMapping("/userrolequery.action")
	@ResponseBody
	public Map userrolequery(Integer page,Integer rows,String userId)
	{
		if(page == null)
			page = 1;
		if(rows == null)
			rows = 10;
		Map m = new HashMap();
		PageHelper.startPage(page,rows);
		List<Map> list = orgmodel.getUserRoleList(userId);
		PageInfo pi = new PageInfo(list);
		Long total = pi.getTotal();
		List<Map> users = pi.getList();
		m.put("total", total);
		m.put("rows", users);
		return m;
	}
	
	@RequestMapping("/userroledel.action")
	@ResponseBody
	public Boolean userroledel(String[] id)
	{
		if(id != null)
		{
			for(String id2:id)
			{
				orgmodel.deleteOrgUserRoleById(id2);
			}
		}
		return true;
	}
	
	@RequestMapping("/usersave.action")
	@ResponseBody
	public Boolean usersave(DefaultUser duser)
	{
		log.debug("usersave duser="+duser);
		String id = duser.getId();
		String pwd = duser.getPassword();
		String md5 = Util.getMD5(pwd);
		log.debug("usersave pwd="+pwd+",md5="+md5);
		duser.setPassword(md5);
		String loginId = duser.getLoginId();
		DefaultUser loginUser = orgmodel.getUserByLoginId(loginId);
		log.debug("usersave loginUser="+loginUser);
		if("".equals(Util.fmtStr(id)))
		{
			if(loginUser != null)
			{
				return false;
			}
			duser.setId(UUID.create("user"));
			orgmodel.saveUser(duser);
		}
		else
		{
			log.debug("usersave id="+id+",loginUser.getId()="+loginUser);
			//说明当前用户对应的LoginId存在两个
			if(loginUser != null && !id.equals(loginUser.getId()))
			{
				return false;
			}
			orgmodel.updateUser(duser);
		}
		return true;
	}
	
	@RequestMapping("/role.action")
	public String role()
	{
		return "/orgmodel/role";
	}
	
	@RequestMapping("/roleload.action")
	@ResponseBody
	public Map roleload(Integer page,Integer rows)
	{
		if(page == null)
			page = 1;
		if(rows == null)
			rows = 10;
		Map m = new HashMap();
		PageHelper.startPage(page,rows);
		List<DefaultRole> list = orgmodel.getAllRole();
		PageInfo pi = new PageInfo(list);
		Long total = pi.getTotal();
		List<DefaultRole> users = pi.getList();
		m.put("total", total);
		m.put("rows", users);
		return m;
	}
	
	@RequestMapping("/rolesave.action")
	@ResponseBody
	public Boolean rolesave(DefaultRole role)
	{
		log.debug("rolesave role="+role);
		String id = role.getId();
		if("".equals(Util.fmtStr(id)))
		{
			id = UUID.create("role");
			role.setId(id);
			orgmodel.saveRole(role);
		}
		else
		{
			orgmodel.updateRole(role);
		}
		return true;
	}
	
	@RequestMapping("/roledelete.action")
	@ResponseBody
	public Boolean roledelete(String[] id)
	{
		log.debug("roledelete id="+id);
		if(id != null)
		{
			for(String id2:id)
				orgmodel.deleteRole(id2);
		}
		return true;
	}
	
	@RequestMapping("/roleuser.action")
	public String orguserrole()
	{
		return "/orgmodel/orguserrole";
	}
	
	@RequestMapping("/roleroot.action")
	@ResponseBody
	public List<TreeNode> roleroot()
	{
		List<TreeNode> rtn = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode();
		root.setId("root");
		root.setText("角色树");
		rtn.add(root);
		List<DefaultRole> roles = orgmodel.getAllRole();
		for(DefaultRole role:roles)
		{
			TreeNode tn = new TreeNode();
			tn.setId(role.getId());
			tn.setText(role.getName());
			root.addChild(tn);
		}

		return rtn;
	}
	
	@RequestMapping("/roleassign.action")
	@ResponseBody
	public Boolean roleassign(String roleId,String userId)
	{
		if(roleId != null && userId != null)
		{
			String[] roleDim = roleId.split(",");
			String[] userDim = userId.split(",");
			for(String roleId2:roleDim)
			{
				for(String userId2:userDim)
				{
					List<DefaultOrgUserRole> chkList = orgmodel.getOrgUserRoleByRoleIdEntityId(roleId2,userId2);
					if(chkList==null || chkList.size() == 0)
					{
						DefaultOrgUserRole dour = new DefaultOrgUserRole();
						String id = UUID.create("dour");
						dour.setId(id);
						dour.setEntityId(userId2);
						dour.setEntityType(DefaultOrgUserRole.USER);
						dour.setRoleId(roleId2);
						orgmodel.saveOrgUserRole(dour);
					}
				}
			}
			return true;
		}
		else
			return false;
	}
	
	@RequestMapping("/module.action")
	public String module()
	{
		return "/orgmodel/module";
	}
	
	@RequestMapping("/moduleload.action")
	@ResponseBody
	public Map loadmodule(Integer page,Integer rows)
	{
		PageHelper.startPage(page, rows);
		List<PermissionInfo> lists = orgmodel.getPermission();
		PageInfo pi = new PageInfo(lists);
		Long total = pi.getTotal();
		List<PermissionInfo> perms = pi.getList();
		Map m = new HashMap();
		m.put("total", total);
		m.put("rows", perms);
		return m;
	}
	
	@RequestMapping("/modulesave.action")
	@ResponseBody
	public Boolean modulesave(PermissionInfo perm)
	{
		String id = perm.getId();
		if("".equals(Util.fmtStr(id)))
		{
			perm.setId(UUID.create("perm"));
			orgmodel.savePermission(perm);
		}
		else
			orgmodel.updatePermission(perm);
		return true;
	}
	
	@RequestMapping("/moduledelete.action")
	@ResponseBody
	public Boolean moduledelete(String[] id)
	{
		if(id != null)
		{
			for(String id2:id)
			{
				orgmodel.deletePermById(id2);
				orgmodel.deletePerm2RoleByPermId(id2);
			}
		}
		return true;
	}
	
	@RequestMapping("/modulerolesave.action")
	@ResponseBody
	public Boolean modulerolesave(String[] permId,String[] roleId)
	{
		if(permId != null && roleId != null)
		{
			for(String permId2:permId)
			{
				orgmodel.deletePerm2RoleByPermId(permId2);
				for(String roleId2:roleId)
				{
					if(!"".equals(Util.fmtStr(permId2)) && !"".equals(Util.fmtStr(roleId2)))
					{
						Perm2RoleInfo p2r = new Perm2RoleInfo();
						p2r.setId(UUID.create("p2r"));
						p2r.setPermId(permId2);
						p2r.setRoleId(roleId2);
						orgmodel.savePerm2Role(p2r);
					}
				}
			}
		}
		return true;
	}
}
