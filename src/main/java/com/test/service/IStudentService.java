package com.test.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.test.bean.HobbyInfo;
import com.test.bean.S2HInfo;
import com.test.bean.StudentInfo;
import com.test.bean.StudentPage;

public interface IStudentService {
	public List<StudentInfo> findStu();
	public StudentPage findPageStu(Integer page,Integer row);
	public StudentInfo findStuById(Integer id);
	public List<HobbyInfo> findHobby();
	public StudentInfo saveStu(StudentInfo si);
	public void saveS2H(S2HInfo s2h);
}