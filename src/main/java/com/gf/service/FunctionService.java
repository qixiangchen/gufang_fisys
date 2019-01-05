package com.gf.service;

import java.util.List;

import com.gf.model.FunctionInfo;

public interface FunctionService {
	public FunctionInfo getRoot();
	public List<FunctionInfo> getMenu(String id);
	public String generateMenuBar(String userId);
	public void recurseFunction(String userId,String id,StringBuffer sb,List<FunctionInfo> aclList);

}
