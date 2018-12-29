package com.gf.statusflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;
import com.gf.statusflow.def.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class OrgModelCtrl {
	private Logger log = LoggerFactory.getLogger(OrgModelCtrl.class); 
	@Autowired
	private IOrgModel orgmodel;
	
	@RequestMapping("/org")
	public String org()
	{
		return "/orgmodel/org";
	}
	
	@RequestMapping("/orgroot")
	@ResponseBody
	public List<TreeNode> orgroot()
	{
		List<TreeNode> roots = new ArrayList<TreeNode>();
		IOrg org = orgmodel.getRootOrg();
		TreeNode tn = fromOrg(org);
		roots.add(tn);
		return roots;
	}
	
	@RequestMapping("/orgchild")
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
	
	@RequestMapping("/orgsubsave")
	@ResponseBody
	public Boolean orgsubsave(DefaultOrg org)
	{
		orgmodel.saveOrg(org);
		return true;
	}
	
	@RequestMapping("/orgsave")
	@ResponseBody
	public Boolean orgsave(DefaultOrg org)
	{
		orgmodel.updateOrg(org);
		return true;
	}
	
	@RequestMapping("/orgload")
	@ResponseBody
	public List<DefaultOrg> orgload()
	{
		return orgmodel.getAllOrg();
	}
	
	@RequestMapping("/orgdelete")
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
	
	@RequestMapping("/user")
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
	
	@RequestMapping("/userquery")
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
	
	@RequestMapping("/usersave")
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
	
	@RequestMapping("/role")
	public String role()
	{
		return "/orgmodel/role";
	}
	
	@RequestMapping("/roleload")
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
	
	@RequestMapping("/rolesave")
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
	
	@RequestMapping("/roledelete")
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
	
	@RequestMapping("/roleuser")
	public String orguserrole()
	{
		return "/orgmodel/orguserrole";
	}
	
	@RequestMapping("/roleroot")
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
}
