package com.gf.statusflow.def;

public class DefStatus
{
	private String id = null;
	private String name = null;
	private String type = null;
	private boolean isStart = false;
	private String excludeRole = null;
	private String workflow = null;
	private String condition = null;
	private String url = null;
	private java.util.List tostatusLst = new java.util.ArrayList();
	private java.util.List actorLst = new java.util.ArrayList();
	private String batchId = null;
	private java.util.List<DefField> fieldLst = new java.util.ArrayList<DefField>();
	private java.util.List<DefButton> buttonLst = new java.util.ArrayList<DefButton>();

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setIsStart(boolean isStart)
	{
		this.isStart = isStart;
	}

	public boolean getIsStart()
	{
		return isStart;
	}

	public String getExcludeRole() {
		return excludeRole;
	}

	public void setExcludeRole(String excludeRole) {
		this.excludeRole = excludeRole;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setToStatusLst(java.util.List tostatusLst)
	{
		this.tostatusLst = tostatusLst;
	}

	public java.util.List getToStatusLst()
	{
		if(tostatusLst == null)
			tostatusLst = new java.util.ArrayList();
		return tostatusLst;
	}

	public void addToStatusLst(DefToStatus dts)
	{
		if(tostatusLst == null)
			tostatusLst = new java.util.ArrayList();
		tostatusLst.add(dts);
	}

	public void setActorLst(java.util.List actorLst)
	{
		this.actorLst = actorLst;
	}

	public java.util.List getActorLst()
	{
		if(actorLst == null)
			actorLst = new java.util.ArrayList();
		return actorLst;
	}

	public void addActorLst(DefActor da)
	{
		if(actorLst == null)
			actorLst = new java.util.ArrayList();
		actorLst.add(da);
	}

	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}

	public String getBatchId()
	{
		return batchId;
	}

	public java.util.List<DefField> getFieldLst() {
		return fieldLst;
	}

	public void setFieldLst(java.util.List<DefField> fieldLst) {
		this.fieldLst = fieldLst;
	}

	public void addFieldLst(DefField df)
	{
		if(fieldLst == null)
			fieldLst = new java.util.ArrayList<DefField>();
		fieldLst.add(df);
	}
	
	public java.util.List<DefButton> getButtonLst() {
		return buttonLst;
	}

	public void setButtonLst(java.util.List<DefButton> buttonLst) {
		this.buttonLst = buttonLst;
	}

	public void addButtonLst(DefButton db)
	{
		if(buttonLst == null)
			buttonLst = new java.util.ArrayList<DefButton>();
		buttonLst.add(db);
	}
	
	public String toString()
	{
		return "DefStatus[id="+id+",name="+name+",type="+type+",tostatusLst="+tostatusLst+",actorLst="+actorLst+"]";
	}

}