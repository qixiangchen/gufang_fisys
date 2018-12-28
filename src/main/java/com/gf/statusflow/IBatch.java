package com.gf.statusflow;

public interface IBatch
{
	public void batchProcess(StatusFlowWAPI sfwf,String instProcessId,
			String instActivityId,String instWorkitemId,String flag2,String testMode) throws Exception;
}