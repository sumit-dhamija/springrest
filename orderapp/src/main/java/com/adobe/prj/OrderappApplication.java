package com.adobe.prj;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.adobe.prj.entity.Customer;
import com.adobe.prj.entity.Product;
import com.adobe.prj.service.OrderService;

@SpringBootApplication
public class OrderappApplication implements CommandLineRunner {
	@Autowired
	private OrderService service;

	public static void main(String[] args) {
		SpringApplication.run(OrderappApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

	@Override
	public void run(String... args) throws Exception {
		List<Product> products = Arrays.asList(new Product(0, "iPhone 12", 89000.00, 100),
				new Product(0, "Reynold Pen", 15.00, 100), new Product(0, "Camlin Marker", 50, 100));

		List<Customer> customers = Arrays.asList(new Customer("a@adobe.com", "Anna"),
				new Customer("b@adobe.com", "Bharath"));

		for (Product p : products) {
			service.addProduct(p);
		}

		for (Customer c : customers) {
			service.addCustomer(c);
		}
	}

}
