package com.gf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Starter extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication springApplication = new SpringApplication(Starter.class);
    	springApplication.addListeners(new ApplicationStartup());
    	springApplication.run(args);
    }
}
