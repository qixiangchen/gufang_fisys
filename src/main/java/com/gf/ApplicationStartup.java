package com.gf;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.gf.statusflow.IOrgModel;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		IOrgModel orgmodel = event.getApplicationContext().getBean(IOrgModel.class);
		orgmodel.initDb();
		orgmodel.initFunc();
	}

}