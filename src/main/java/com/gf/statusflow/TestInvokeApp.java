package com.gf.statusflow;


import java.util.List;

import com.gf.statusflow.def.DefWorkItem;

public class TestInvokeApp implements InvokeApp
{
	public StatusMsg beforeInvoke(StatusFlowWAPI wapi,String command,
		String defProcessId,String type,String instanceId,String fromUserId,java.util.List toUserIdLst,String title,
		String url,String fromStatusId,String toStatusId,String flag2,String testMode) throws Exception
	{
		System.out.println("wapi = "+wapi);
		System.out.println("command = "+command);
		System.out.println("defProcessId = "+defProcessId);
		System.out.println("type = "+type);
		System.out.println("instanceId = "+instanceId);
		System.out.println("fromUserId = "+fromUserId);
		System.out.println("toUserIdLst = "+toUserIdLst);
		System.out.println("title = "+title);
		System.out.println("url = "+url);
		System.out.println("fromStatusId = "+fromStatusId);
		System.out.println("toStatusId = "+toStatusId);
		
		return null;
	}

	@Override
	public StatusMsg afterInvoke(StatusFlowWAPI wapi, String command,
			String defProcessId, String type, String instanceId,
			String instProcessId,String instActivityId,java.util.List<DefWorkItem> dwiList,
			String fromUserId, java.util.List toUserIdLst, String title, String url,
			String fromStatusId, String toStatusId,String flag2,String testMode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}