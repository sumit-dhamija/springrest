package com.example.demo.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
//@ConditionalOnProperty(name = "productdao", havingValue = "redis")
@Profile("dev")
public class ProductDaoRedisImpl implements ProductDao {

	@Override
	public void addProduct() {
		System.out.println("Redis!!!");
	}

}
