package com.gf.statusflow;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class StatusFlowData {
	private StatusMsg wfMsg = null;
	private List<Properties> nextBtnList = null;
	private List<Properties> nextActList = null;
	private List<Properties> nextUserSelectList = null;
	public StatusMsg getWfMsg() {
		return wfMsg;
	}
	public void setWfMsg(StatusMsg wfMsg) {
		this.wfMsg = wfMsg;
	}
	public List<Properties> getNextBtnList() {
		return nextBtnList;
	}
	public void setNextBtnList(List<Properties> nextBtnList) {
		this.nextBtnList = nextBtnList;
	}
	public List<Properties> getNextActList() {
		return nextActList;
	}
	public void setNextActList(List<Properties> nextActList) {
		this.nextActList = nextActList;
	}
	public List<Properties> getNextUserSelectList() {
		return nextUserSelectList;
	}
	public void setNextUserSelectList(List<Properties> nextUserSelectList) {
		this.nextUserSelectList = nextUserSelectList;
	}
	
}
