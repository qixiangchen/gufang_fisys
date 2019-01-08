package com.gf.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gf.model.ExpenseInfo;

@Mapper
public interface ExpenseMapper extends BaseMapper<ExpenseInfo>{

}
