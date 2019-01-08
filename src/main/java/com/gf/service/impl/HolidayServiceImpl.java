package com.gf.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gf.mapper.ExpenseMapper;
import com.gf.mapper.HolidayMapper;
import com.gf.model.ExpenseInfo;
import com.gf.model.HolidayInfo;
import com.gf.service.ExpenseService;
import com.gf.service.HolidayService;

@Service
public class HolidayServiceImpl extends ServiceImpl<HolidayMapper,HolidayInfo>
	implements HolidayService
{

}
