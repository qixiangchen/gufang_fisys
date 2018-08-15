package com.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.test.bean.HobbyInfo;
import com.test.bean.S2HInfo;
import com.test.bean.StudentInfo;

public interface StuMapper {
	public List<StudentInfo> findStu();
	public StudentInfo findStuById(@Param("id") Integer id);
	public List<HobbyInfo> findHobby();
	public void saveStu(StudentInfo si);
	public void saveS2H(S2HInfo s2h);
}
