package com.gf.statusflow;

import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DependenceUtil
{
	@Autowired
	private IOrgModel orgmodel;
	
	public static String fmtStr(Object obj)
	{
		if(obj == null)
			return "";
		return obj.toString();
	}	
	
	public String getHome() throws Exception
	{
		String home = Util.getGuFangHome();
		return home;
	}

	public String createId(String prefix)
	{
		return UUID.create(prefix);
	}

	public String getManagerId(String userId)
	{
		IUser user = orgmodel.getUserById(userId);
		if(user == null)
			return null;
		return Util.fmtStr(user.getManagerId());
	}	
	
	public boolean isExistUserId(String userId)
	{
		IUser user = orgmodel.getUserById(userId);
		if(user == null)
			return false;
		return true;
	}

	public String getUserName(String userId)
	{
		IUser user = orgmodel.getUserById(userId);
		if(user == null)
			return "";
		return Util.fmtStr(user.getName());
	}
	
	public IUser getUserNameByLoginId(String loginId)
	{
		//Properties prop = Util.getLoginProp(loginId);
		//String flag = prop.getProperty("flag");
		//String loginId2 = prop.getProperty("loginid");
		String flag = "gf";
		IUser user = orgmodel.getUserByLoginId(loginId);
		if(user == null)
			return null;
		return user;
	}

	public java.util.List getOrgUserList(String orgId)
	{
		java.util.List rtn = new java.util.ArrayList();
		if(orgId == null || "".equals(orgId))
			return rtn;
		IOrg org = orgmodel.getOrgById(orgId);
		java.util.List lst = orgmodel.getUserListByOrgId(orgId,org.getFlag());
		if(lst != null)
		{
			for(java.util.Iterator it=lst.iterator();it.hasNext();)
			{
				IUser user = (IUser)it.next();
				rtn.add(user.getId());
			}
		}
		if(rtn == null)
			rtn = new java.util.ArrayList();
		return rtn;
	}
	
	public java.util.List getRoleUserList(String roleId)
	{
		java.util.List rtn = new java.util.ArrayList();
		if(roleId == null || "".equals(roleId))
			return rtn;
		Properties prop = Util.getRoleProp(roleId);
		String flag = prop.getProperty("flag");
		String name = prop.getProperty("name");
		IRole role = orgmodel.getRoleByNameFlag(name,flag);
		if(role == null)
			return rtn;
		List<IOrgUserRole> ourLst = orgmodel.getOrgUserRoleListByRoleId(role.getId());
		for(IOrgUserRole ouri:ourLst)
		{
			if("USER".equalsIgnoreCase(ouri.getEntityType()))
				rtn.add(ouri.getEntityId());
		}
		if(rtn == null)
			rtn = new java.util.ArrayList();
		return rtn;
	}

}