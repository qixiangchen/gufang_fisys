package com.gf.statusflow;

import java.util.*;

import com.gf.statusflow.def.*;

public class PagedWorkItem
{
	private long count = 0;
	private int currentPage = 0;
	private int pages = 0;
	private List workItemLst = new ArrayList();
	
	public void setCount(long count)
	{
		this.count = count;
	}

	public long getCount()
	{
		return count;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setPages(int pages)
	{
		this.pages = pages;
	}

	public int getPages()
	{
		return pages;
	}

	public void setWorkItemList(List workItemLst)
	{
		this.workItemLst = workItemLst;
	}

	public List getWorkItemList()
	{
		if(workItemLst == null)
			workItemLst = new ArrayList();
		return workItemLst;
	}

	public void addWorkItem(DefWorkItem dwi)
	{
		if(workItemLst == null)
			workItemLst = new ArrayList();
		workItemLst.add(dwi);
	}
}