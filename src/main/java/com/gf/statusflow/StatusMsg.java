package com.gf.statusflow;

public class StatusMsg {
	private boolean isOk = true;
	private String code = "WF000000";
	private String msg = null;
	private String instanceId = null;
	private String instProcessId = null;
	private String instActivityId = null;
	private String instWorkitemId = null;
	
	public boolean isOk() {
		return isOk;
	}
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstProcessId() {
		return instProcessId;
	}
	public void setInstProcessId(String instProcessId) {
		this.instProcessId = instProcessId;
	}
	public String getInstActivityId() {
		return instActivityId;
	}
	public void setInstActivityId(String instActivityId) {
		this.instActivityId = instActivityId;
	}
	public String getInstWorkitemId() {
		return instWorkitemId;
	}
	public void setInstWorkitemId(String instWorkitemId) {
		this.instWorkitemId = instWorkitemId;
	}
	public String toString()
	{
		return "StatusMsg[isOk="+isOk+",code="+code+",msg="+msg+"]";
	}
}
