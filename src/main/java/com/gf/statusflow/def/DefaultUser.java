package com.gf.statusflow.def;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.gf.statusflow.IUser;

public class DefaultUser implements IUser{
	private String id = null;
	private String loginId = null;
	private String password = null;
	private String name = null;// /1/2
	private String address = null;// /集团公司/古方红糖厂家/财务部
	private String companyMail = null;
	private String privateMail = null;
	private String companyTeleNo = null;
	private String homeTeleNo = null;
	private String mobile = null;
	private String title = null;
	private String desc = null;
	private String orgId = null;
	private String orgPath = null;
	private boolean enabled = true;
	private String locked = null;
	private String managerId = null;
	private Date birthday = null;
	private String cardId = null;
	private Date failureDate = null;
	private Integer failureCount= null;
	private String openid = null;
	private Integer seqno = null;
	private String flag = "gf";
	private String testMode = null;
	private List<String> roleList = new ArrayList<String>();
	
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getLoginId() {
		return loginId;
	}
	@Override
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getAddress() {
		return address;
	}
	@Override
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String getCompanyMail() {
		return companyMail;
	}
	@Override
	public void setCompanyMail(String companyMail) {
		this.companyMail = companyMail;
	}
	@Override
	public String getPrivateMail() {
		return privateMail;
	}
	@Override
	public void setPrivateMail(String privateMail) {
		this.privateMail = privateMail;
	}
	@Override
	public String getCompanyTeleNo() {
		return companyTeleNo;
	}
	@Override
	public void setCompanyTeleNo(String companyTeleNo) {
		this.companyTeleNo = companyTeleNo;
	}
	@Override
	public String getHomeTeleNo() {
		return homeTeleNo;
	}
	@Override
	public void setHomeTeleNo(String homeTeleNo) {
		this.homeTeleNo = homeTeleNo;
	}
	@Override
	public String getMobile() {
		return mobile;
	}
	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String getDesc() {
		return desc;
	}
	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String getOrgId() {
		return orgId;
	}
	@Override
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	@Override
	public String getOrgPath() {
		return orgPath;
	}
	@Override
	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}
	@Override
	public boolean getEnabled() {
		return enabled;
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String getLocked() {
		return locked;
	}
	@Override
	public void setLocked(String locked) {
		this.locked = locked;
	}
	@Override
	public String getManagerId() {
		return managerId;
	}
	@Override
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	@Override
	public Date getBirthday() {
		return birthday;
	}
	@Override
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Override
	public String getCardId() {
		return cardId;
	}
	@Override
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	@Override
	public Date getFailureDate() {
		return failureDate;
	}
	@Override
	public void setFailureDate(Date failureDate) {
		this.failureDate = failureDate;
	}
	@Override
	public Integer getFailureCount() {
		return failureCount;
	}
	@Override
	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}
	@Override
	public String getOpenid() {
		return openid;
	}
	@Override
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Override
	public Integer getSeqno() {
		return seqno;
	}
	@Override
	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}
	@Override
	public String getFlag() {
		return flag;
	}
	@Override
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public List<String> getRoleList() {
		return roleList;
	}
	@Override
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	@Override
	public String getTestMode() {
		return testMode;
	}
	@Override
	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}
	
	public String toString()
	{
		return "DefaultUser[id="+id+",loginId="+loginId+",password="+password+",name="+name+",address="+address+",companyMail="+companyMail
			+",privateMail="+privateMail+",companyTeleNo="+companyTeleNo+",homeTeleNo="+homeTeleNo+",mobile="+mobile
			+",title="+title+",desc="+desc+",orgId="+orgId+",orgPath="+orgPath+",enabled="+enabled+",locked="+locked
			+",managerId="+managerId+",birthday="+birthday+",cardId="+cardId+",failureDate="+failureDate+",failureCount="+failureCount
			+",openid="+openid+",seqno="+seqno+",flag="+flag+",testMode="+testMode+"]";
	}
}
