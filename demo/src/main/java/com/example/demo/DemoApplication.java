package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demo.service.OrderService;
import com.example.demo.util.DBUtil;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		OrderService ser = ctx.getBean("orderService", OrderService.class);
		ser.doTask();
		
//		String[] names = ctx.getBeanDefinitionNames();
//		for(String name : names) {
//			System.out.println(name);
//		}
//		
		DBUtil util = ctx.getBean("getDbUtil",DBUtil.class);
		System.out.println(util.getUrl());
	}

}
