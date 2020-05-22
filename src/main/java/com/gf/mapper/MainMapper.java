package com.gf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gf.model.FunctionInfo;
import com.gf.statusflow.def.DefaultOrg;

//add git
@Mapper
public interface MainMapper {
	public FunctionInfo getRoot();
	public List<FunctionInfo> getMenu(@Param("parentId") String id);


}
