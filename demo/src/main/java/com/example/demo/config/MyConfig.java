package com.example.demo.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.util.DBUtil;


@Configuration
public class MyConfig {
	// spEL
	@Value("#{systemProperties}")
	private Map<String, String> properties;
	
	@Value("#{systemProperties['user.name']}")
	String systemUser;
	
	@Bean
	public DBUtil getDbUtil(@Value("${url}") String url,  @Value("${user:root}") String user,  @Value("${pwd}") String pwd) {
		DBUtil util = new DBUtil(url, user, pwd);
		System.out.println(properties);
		System.out.println("*******");
		System.out.println(systemUser);
		System.out.println("*********");
		return util;
	}
	
//	@Bean
//	@ConditionalOnMissingBean(ProductDaoJpaImpl.class)
//	public ProductDao getProductDao() {
//		return new ProductDao() {
//
//			@Override
//			public void addProduct() {
//				// 
//			}
//			
//		};
//	}
}
