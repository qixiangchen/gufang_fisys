package com.gf.acl;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.IOrgUserRole;
import com.gf.statusflow.IRole;
import com.gf.statusflow.IUser;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.PermissionInfo;

public class GfShiroRealm extends AuthorizingRealm{
	private Logger log = LoggerFactory.getLogger(GfShiroRealm.class);
	
	@Autowired
	private IOrgModel orgmodel;
	
	//角色权限和对应权限添加
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
	{
		//获取登录用户名
		String loginId= (String) principalCollection.getPrimaryPrincipal();
		//查询用户名称
		IUser user = orgmodel.getUserByLoginId(loginId);
		
		
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//判断是否为超级管理员
		if(IOrgModel.I_SYSADMIN.equals(loginId))
		{
			for (PermissionInfo perm:orgmodel.getPermission("","","")) {
				//添加权限
				simpleAuthorizationInfo.addStringPermission(perm.getPermission());
			}
		}
		else//普通用户获取权限列表
		{
			//添加角色和权限
			//部门与角色，用户与角色关系表对应实体类IOrgUserRole
			List<DefaultOrgUserRole> roles = orgmodel.getOrgUserRoleByEntityId(user.getId());
			for(DefaultOrgUserRole dour:roles)
			{
				for (String perm:orgmodel.getPermByRoleId(dour.getRoleId())) {
					//添加权限
					simpleAuthorizationInfo.addStringPermission(perm);
				}
			}
		}
		return simpleAuthorizationInfo;
	}
	
	//用户认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) 
			throws AuthenticationException
	{
		//加这一步的目的是在Post请求的时候会先进认证，然后在到请求
		if (authenticationToken.getPrincipal() == null) {
			return null;
		}
		//获取用户信息
		String loginId = authenticationToken.getPrincipal().toString();
		IUser user = orgmodel.getUserByLoginId(loginId);
		if (user == null) {
			//这里返回后会报出对应异常
			return null;
		} else {
			//这里验证authenticationToken和simpleAuthenticationInfo的信息
			SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(loginId, user.getPassword().toString(),user.getName());
			return simpleAuthenticationInfo;
		}
	}

}
