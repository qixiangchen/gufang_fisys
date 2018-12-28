package com.gf.statusflow;

import com.gf.statusflow.def.*;

public class BatchProcess implements IBatch
{
	public void batchProcess(StatusFlowWAPI sfwf,String instProcessId,
			String instActivityId,String instWorkitemId,String flag2,String testMode) throws Exception
	{
		StatusMsg msg = new StatusMsg();
		//如果instWorkitemId == null退出
		if(instWorkitemId == null || "".equals(instWorkitemId))
			return;
		try
		{
			//如果下一环节不存在预定义参与者退出
			boolean isNextTaskDefineActor = sfwf.isNextTaskDefineActor(instWorkitemId,msg);
			if(!isNextTaskDefineActor)
				return;
			//获取当前instWorkitemId对应对象
			DefWorkItem dwi = sfwf.getWorkItem(instWorkitemId);
			String instanceId = dwi.getInstanceId();
			String url = dwi.getUrl();
			String type = dwi.getType();
			String title = dwi.getTitle();
			//取得当前活动节点
			DefStatus defStus = sfwf.getStatusByInstActivityId(instActivityId,msg);
			String nextTask = defStus.getBatchId();
			//如果下一环节为空退出
			if(nextTask == null || "".equals(nextTask))
				return;
			java.util.HashMap hmap = StatusFlowUtil.getData(dwi.getXmlData());
			//String no = (String)hmap.get("no");
			//String amount = (String)hmap.get("amount");
			//java.util.HashMap hmap2 = new java.util.HashMap();
			//hmap2.put("no",no);
			//hmap2.put("amount",amount);
			java.util.List userIdLst = new java.util.ArrayList();
			sfwf.submitWorkflow(instProcessId,instActivityId,instWorkitemId,userIdLst,
					instanceId,type,title,nextTask,url,hmap,flag2,testMode);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}