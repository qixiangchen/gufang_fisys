package com.gf.statusflow;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gf.statusflow.def.DefWorkItem;
import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.DefaultOrgUserRole;
import com.gf.statusflow.def.DefaultRole;
import com.gf.statusflow.def.DefaultUser;

@Mapper
public interface WorkflowMapper {
	/**
	 * 部门相关Mybatis方法
	 * @return
	 */
	public List<DefaultOrg> getAllOrg();
	public DefaultOrg getRootOrg();
	public void saveOrg(DefaultOrg org);
	public void deleteOrg(@Param("id") String id);
	public void updateOrg(DefaultOrg org);
	public List<DefaultOrg> getOrgList(@Param("id") String id);
	public DefaultOrg getOrgById(@Param("id") String id);
	
	/**
	 * 用户相关Mybatis方法
	 * @param dwi
	 */
	public void saveUser(DefaultUser user);
	public void updateUser(DefaultUser user);
	public void deleteUser(@Param("id") String id);
	public List<DefaultUser> findUserByName(@Param("qname") String qname);
	public List<DefaultUser> getUserByOrgId(@Param("orgId") String orgId);
	public DefaultUser getManagerById(@Param("userId") String userId);
	public List<DefaultUser> getUserByManagerId(@Param("managerId") String managerId);
	public DefaultUser checkLogin(@Param("loginId") String loginId,
			@Param("password") String password);
	public DefaultUser getUserByLoginId(@Param("loginId") String loginId);
	
	/**
	 * 角色相关Mybatis方法
	 */
	public void saveRole(DefaultRole role);
	public void updateRole(DefaultRole role);
	public void deleteRole(@Param("id") String id);
	public List<DefaultRole> getAllRole();
	public DefaultRole getRoleById(@Param("id") String id);
	
	/**
	 * 部门与角色，用户与角色相关SQ
	 */
	public void saveOrgUserRole(DefaultOrgUserRole our);
	public void deleteOrgUserRoleByEntityId(@Param("entityId") String entityId);
	public List<DefaultOrgUserRole> getOrgUserRoleByEntityId(@Param("entityId") String entityId);
	
	/**]
	 * 工作流相关Mybatis方法
	 * @param dwi
	 */
	public void saveWorkitem(DefWorkItem dwi);
	public void updateWorkitem(DefWorkItem dwi);
	public void deleteWorkitem(String id);
	public DefWorkItem getWorkitemById(@Param("id") String id);
	public List<String> getProcessId(@Param("instProcessId") String instProcessId);
	public String getStatusId(@Param("instActivityId") String instActivityId);
	public String getProcessUserId(@Param("instProcessId") String instProcessId,
			@Param("instActivityId") String instActivityId);
	public List<DefWorkItem> getSameActivityWorkitem(@Param("instProcessId") String instProcessId,
			@Param("instActivityId") String instActivityId);
	
	public List<DefWorkItem> getUndoWorkitemList(@Param("userId") String userId,
			@Param("testMode") String testMode,@Param("flag") String flag);
	public List<DefWorkItem> getWorkitemList(@Param("userId") String userId,
			@Param("testMode") String testMode);
	public List<DefWorkItem> getFinishList(@Param("userId") String userId,
			@Param("testMode") String testMode);
	public List<DefWorkItem> getTraceList(@Param("instProcessId") String instProcessId,
			@Param("flag") String flag);
	public List<DefWorkItem> getTraceListByInstanceId(@Param("instanceId") String instanceId,
			@Param("flag") String flag);
	public List<DefWorkItem> getQueryWorkitemList(@Param("userId") String userId,
			@Param("xmlData") String xmlData,@Param("startUserId") String startUserId,
			@Param("title") String title,@Param("createTime1") java.sql.Timestamp createTime1,
			@Param("createTime2") java.sql.Timestamp createTime2,
			@Param("statusName") String statusName,@Param("testMode") String testMode);
	public List<DefWorkItem> getQueryFinishList(@Param("userId") String userId,
			@Param("xmlData") String xmlData,@Param("startUserId") String startUserId,
			@Param("title") String title,@Param("createTime1") java.sql.Timestamp createTime1,
			@Param("createTime2") java.sql.Timestamp createTime2,
			@Param("statusName") String statusName,@Param("testMode") String testMode);
	public List<DefWorkItem> getWorkitemListByInstActivityId(@Param("instActivityId") String instActivityId);
	public String getLastStatusName(@Param("instanceId") String instanceId);
	public List<DefWorkItem> getWorkItemListByInstanceId(@Param("instanceId") String instanceId);
	public List<DefWorkItem> getWorkItemListByInstProcessId(@Param("instProcessId") String instProcessId);
	public List<DefWorkItem> getDraftWorkItemListByInstanceId(@Param("instanceId") String instanceId,
			@Param("flag") String flag);
	public List<DefWorkItem> getWorkItemListByInstanceIdAndStatusId(@Param("instanceId") String instanceId,
			@Param("statusId") String statusId);
	public String getInstProcessIdByInstanceId(@Param("instanceId") String instanceId);
	public List<DefWorkItem> getUndoWorkItem(@Param("instanceId") String instanceId,
			@Param("userId") String userId);
	public List<DefWorkItem> getWorkItemByInstanceIdAndFlag(@Param("instanceId") String instanceId,
			@Param("flag") String flag);
	public List<DefWorkItem> getWorkItemByInstanceIdAndStatusIdAndFlag(@Param("instanceId") String instanceId,
			@Param("statusId") String statusId,@Param("flag") String flag);
	public List<DefWorkItem> getWorkItemByInstanceIdAndStatusId(@Param("instanceId") String instanceId,
			@Param("statusId") String statusId);
	public List<DefWorkItem> getWorkItemListByUidPidAidWid(@Param("userId") String userId,
			@Param("instProcessId") String instProcessId,@Param("instActivityId") String instActivityId,
			@Param("instWorkitemId") String instWorkitemId);
	public List<DefWorkItem> getWorkItemListByPidAidWid(@Param("instProcessId") String instProcessId,
			@Param("instActivityId") String instActivityId,
			@Param("instWorkitemId") String instWorkitemId);
}
