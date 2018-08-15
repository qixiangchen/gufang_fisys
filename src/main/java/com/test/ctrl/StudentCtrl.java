package com.test.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.bean.HobbyInfo;
import com.test.bean.StudentInfo;
import com.test.bean.StudentPage;
import com.test.rabbitmq.SendMsg;
import com.test.service.IStudentService;
import com.test.util.PageUtil;

@Controller
public class StudentCtrl {
	@Autowired
	private IStudentService serv;
	@Autowired
	private SendMsg sendmsg;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest req,Integer page,Integer rows)
	{
		if(page == null)
			page = 1;
		if(rows == null)
			rows = 4;
		StudentPage sp = serv.findPageStu(page, rows);
		List<StudentInfo> students = sp.getList();
		Integer total = sp.getTotal();
		
		PageUtil pu = new PageUtil("/list",page,rows,total.longValue());
		String pageDiv = pu.toHtml();
		
		req.setAttribute("students", students);
		req.setAttribute("pageDiv", pageDiv);
		
		return "student";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public boolean save(HttpServletRequest req,StudentInfo si)
	{
		System.out.println("Controller hid === "+si.getHid());
		String exg = "springrabbit_exg";
		String key = "direct";
		sendmsg.sendObject(exg, key, si);
		return true;
	}
	
	@RequestMapping("/add")
	public String add(HttpServletRequest req)
	{
		List<HobbyInfo> hobby = serv.findHobby();
		req.setAttribute("hobby", hobby);
		return "add";
	}
	
	@RequestMapping("/view")
	public String view(HttpServletRequest req,Integer id)
	{
		StudentInfo si = serv.findStuById(id);
		String hid = si.getHid();
		System.out.println("hid==="+hid);
		List<HobbyInfo> hobby = serv.findHobby();
		for(HobbyInfo hi:hobby)
		{
			if(hid.indexOf(hi.getId().toString())>=0)
			{
				hi.setChecked("checked");
			}
		}
		
		req.setAttribute("hobby", hobby);
		req.setAttribute("stu", si);
		req.setAttribute("view", "true");
		
		return "add";
	}
}
