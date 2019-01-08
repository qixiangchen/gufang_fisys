<%
byte[] data2 = null;
String fileName = "code.png";
String contextType = "image/png";
String processid = com.gf.statusflow.Util.getString(request,"processid");
String instanceid = com.gf.statusflow.Util.getString(request,"instanceid");
String instprocessid = com.gf.statusflow.Util.getString(request,"instprocessid");
int width = com.gf.statusflow.Util.getInteger(request,"width");
int height = com.gf.statusflow.Util.getInteger(request,"height");
System.out.println("processid="+processid);
System.out.println("instanceid="+instanceid);
System.out.println("instprocessid="+instprocessid);
System.out.println("width="+width);
System.out.println("height="+height);
if(width == 0)
	width = 400;
if(height == 0)
	height = 400;
com.gf.statusflow.StatusFlowWAPI wapi = (com.gf.statusflow.StatusFlowWAPI)com.gf.statusflow.Util.getBean(com.gf.statusflow.StatusFlowWAPI.class);
if(!"".equals(instanceid))
{
	java.util.List<String> instanceIdList = new java.util.ArrayList<String>();
	instanceIdList.add(instanceid);
	instprocessid = wapi.getInstProcessIdByInstanceId(instanceid);
	if(instprocessid == null)
		data2 = wapi.getUiImage(processid,instanceIdList,width,height);
	else
		data2 = wapi.getUiImageByInstProcessId(processid,instprocessid,width,height);
}
else
{
	data2 = wapi.getUiImageByInstProcessId(processid,instprocessid,width,height);
}

response.setHeader("Pragma","no-cache");
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires",-1);
try
{
	fileName = new String(fileName.getBytes("GBK"),"ISO8859-1");
	response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
	response.setContentType(contextType);
	java.io.OutputStream os = response.getOutputStream();
	os.write(data2,0,data2.length);
	os.flush();
}
catch(Exception e)
{
	e.printStackTrace();
}
%>