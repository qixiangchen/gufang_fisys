package com.gf.statusflow;

import com.gf.statusflow.def.DefWorkItem;

public interface InvokeApp
{
	public StatusMsg beforeInvoke(StatusFlowWAPI wapi,String command,String defProcessId,
			String type,String instanceId,String fromUserId,
			java.util.List toUserIdLst,String title,
			String url,String fromStatusId,String toStatusId,String flag2,String testMode) throws Exception;
	
	public StatusMsg afterInvoke(StatusFlowWAPI wapi,String command,String defProcessId,
			String type,String instanceId,String instProcessId,String instActivityId,
			java.util.List<DefWorkItem> dwiList,String fromUserId,
			java.util.List toUserIdLst,String title,
			String url,String fromStatusId,String toStatusId,String flag2,String testMode) throws Exception;
	
}