package com.gf.statusflow;

import java.sql.Date;
import java.util.List;


public interface IUser extends java.io.Serializable
{
	public void setId(String id);
	public String getId();
	public void setLoginId(String loginId);
	public String getLoginId();
	public void setPassword(String password);
	public String getPassword();
	public void setName(String name);
	public String getName();
	public void setAddress(String address);
	public String getAddress();
	public void setCompanyMail(String companyMail);
	public String getCompanyMail();
	public void setPrivateMail(String privateMail);
	public String getPrivateMail();	
	public void setCompanyTeleNo(String companyTeleNo);
	public String getCompanyTeleNo();
	public void setHomeTeleNo(String homeTeleNo);
	public String getHomeTeleNo();
	public void setMobile(String mobile);
	public String getMobile();
	public void setTitle(String title);
	public String getTitle();
	public void setDesc(String desc);
	public String getDesc();
	public void setOrgId(String orgId);
	public String getOrgId();
	public void setOrgPath(String orgPath);
	public String getOrgPath();
	public void setEnabled(boolean enabled);
	public boolean getEnabled();
	public void setLocked(String locked);
	public String getLocked();
	public void setManagerId(String managerId);
	public String getManagerId();
	public Date getBirthday();
	public void setBirthday(Date birthday);
	public String getCardId();
	public void setCardId(String cardId);
	public Date getFailureDate();
	public void setFailureDate(Date failureDate);
	public Integer getFailureCount();
	public void setFailureCount(Integer failureCount);
	public void setOpenid(String openid);
	public String getOpenid();
	public void setSeqno(Integer seqno);
	public Integer getSeqno();
	public void setFlag(String flag);
	public String getFlag();
	public void setRoleList(List<String> roleList);
	public List<String> getRoleList();
	public void setTestMode(String testMode);
	public String getTestMode();
}