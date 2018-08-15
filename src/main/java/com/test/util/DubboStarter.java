package com.test.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboStarter {

	public static void main(String[] args) {
		try
		{
			ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("dubbo_service.xml");
			ctx.start();
			System.out.println("Dubbo服务启动");
			System.in.read();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
