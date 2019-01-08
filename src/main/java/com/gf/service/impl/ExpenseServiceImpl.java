package com.gf.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gf.mapper.ExpenseMapper;
import com.gf.model.ExpenseInfo;
import com.gf.service.ExpenseService;

@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper,ExpenseInfo>
	implements ExpenseService
{

}
