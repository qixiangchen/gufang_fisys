package com.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.bean.HobbyInfo;
import com.test.bean.S2HInfo;
import com.test.bean.StudentInfo;
import com.test.bean.StudentPage;
import com.test.mapper.StuMapper;
import com.test.service.IStudentService;

public class StudentServiceImpl implements IStudentService{
	@Autowired
	private StuMapper mapper;

	//检索全部的学生
	@Override
	public List<StudentInfo> findStu() {
		return mapper.findStu();
	}

	//根据学生ID查询学生对象，
	@Override
	public StudentInfo findStuById(Integer id) {
		return mapper.findStuById(id);
	}

	//检索全部的爱好
	@Override
	public List<HobbyInfo> findHobby() {
		return mapper.findHobby();
	}

	//保存学生对象到数据库
	@Override
	public StudentInfo saveStu(StudentInfo si) {
		try
		{
			mapper.saveStu(si);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return si;
	}

	//分页查询学生对象
	@Override
	public StudentPage findPageStu(Integer page, Integer row) {
		PageHelper.startPage(page,row);
		List<StudentInfo> lst = mapper.findStu();
		PageInfo pi = new PageInfo(lst);
		
		List<StudentInfo> lst2 = pi.getList();
		Integer total = new Long(pi.getTotal()).intValue();
		StudentPage spage = new StudentPage();
		spage.setList(lst2);
		spage.setTotal(total);
		
		return spage;
	}

	@Override
	public void saveS2H(S2HInfo s2h) {
		mapper.saveS2H(s2h);
	}

}
