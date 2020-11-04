# Spring Boot; Building RESTful web services
Banuprakash C

Full Stack Architect,

Co-founder Lucida Technologies Pvt Ltd.,

Corporate Trainer,

Email: banu@lucidatechnologies.com; banuprakashc@yahoo.co.in

https://www.linkedin.com/in/banu-prakash-50416019/

https://github.com/BanuPrakash/springrest

-----------------------------------------------------

Softwares Required:
1) Java 8+
2) Eclipse for JEE / IntelliJ for Enterprise
3) MySQL / h2 memory database
4) Docker for Desktop [ Redis, Promethus]

-------------------------------------------------------------------------

Spring Framework
	a lightweight container for building enterprise application.
	==> Dependency Injection [ inversion of Control ]
	==> Redis made Templates [ Enterprise Application Integration]
	==> Simplies Web application / RESTful API development

	All beans are Singleton by default in spring container;
	We have option to mention as Prototype, RequestScope, SessionScope, ApplicationScope

	Prototype ==> one per wiring
	Request ==> one per request [ web]
	Session ==> one per user session
	application ==> one per application
-------------------------------------------------------

	Metadata as xml

	interface ProductDao {}
	
	public class ProductDaoJpaImpl implements ProductDao {}

	public class OrderService { 
		ProductDao productDao;
		OrderService(ProductDao pd) {}
		public void setReference(ProductDao pdao) {}
	}


	<bean id="pd" class="pkg.ProductDaoJpaImpl" scope="prototype" /> <!-- instantiates ProductDaoJpaImpl as productDao -->


	<bean id="service" class="pkg.OrderService" /> <!-- instance of OrderService identified by "service" -->

	wiring

	beans.xml:
	<bean id="service" class="pkg.OrderService">
		<property name="reference" ref="pd" /> <!-- setter injection -->
	</bean>

	<bean id="service" class="pkg.SomeService">
		<property name="reference" ref="pd" /> <!-- setter injection -->
	</bean>
 	---------

 	<bean id="service" class="pkg.OrderService">
		<constructor index="0" ref="pd" /> <!-- constructor injection -->
	</bean>

	----------------------------------------------------

	<bean id="service" class="pkg.OrderService" autowire="byType"  /> 
	<!-- looks at all setters and injects if argument type is available in container -->

-------------------------------------------------------------------------------------------------

Metadata ==> Annotations

	Spring instantiates the beans if it has one of these annotations at class level:

	1) @Component
	2) @Service
	3) @Repository
		Repository / DAO code
	4) @Configuration
	5) @Controller
		MVC
	6) @RestController
		RESTful ==> Additional features for converting data to represention

	@Component
	public class DBUtil {

	}

	Spring creates instance of DBUtil as "dbUtil"

	Eclipse Market Place ==> Spring tool suite [ STS  4]
	Eclipse ==> Spring Starter Project
	IntelliJ ==> Spring Initializer

---------------------------------------------------

	Spring Boot 2.3.5 version ==> Framework on top of Spring Framework 5.2 version

	Why Spring Boot?
		1) Simplifies application development
		2) Out of the box configuration
		3) Most of the scaffolding code is not required
		4) Makes the application compatable to deployed on container [ MicroServices]

==> Using Spring boot we have starting point same for console or web based application:

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

if it is based based ==> it starts web server and deploys the application on that web server.

@SpringBootApplication
	is having 3 parts
		1) @Configuration
		2) @ComponentScan
			scans for above mentioned annoations on top  of class and instantiates 
			search will be from "com.example.demo" package onwards
			example: com.example.demo.repo ; com.example.demo.service is eligible

			com.example.sample is not eligible


			If it was Spring Framework: @ComponentScan("com.example.demo")

		3) @EnableAutoConfiguration
			scans for class in "jar" dependencies and instantiates them

	--
	SpringApplication.run creates ApplicationContext ==> ApplicationContext is a spring Container

	If not for Spring boot:

 	ApplicationContext ctx = new ClassPathApplicationContext("beans.xml");

	ApplicationContext ctx = new AnnotationConfigApplicationContext();



	@Autowired
	private ProductDao productDao;

	Spring looks for instance of ProductDao ===> ProductDaoJpaImpl and wires it using Bytecode instrumentation

	Bytecode instrumentation: CGLib and Javaassit are byte code instrumentation libraries


	private ProductDao productDao = new ProductDaoJpaImpl();

	@Primary to resolve ambiguos situation

	============

	Resolve Properties
		1) command Line arguments

			java FileName --productdao=jpa

		2) application_profile.properties
		3) application.properties


		4) --spring.profiles.active=dev
		applciation_dev.properties
		applciation_prod.properties
		applciation_qa.properties

		@ConditionalOnProperty(name = "productdao", havingValue = "redis")
		public class ProductDaoJpaImpl implements ProductDao {
		}

		5)
		--spring.profiles.active=dev
		@Profile("prod")
		public class ProductDaoJpaImpl implements ProductDao {

		}

		@Profile("dev")
		public class ProductDaoRedisImpl implements ProductDao {
		}

===================================
	
	@Bean
	1) Using classes from 3rd party libraries 
	2) instance of the class depends on our explict arguments


	@Configuration
	@PropertyPlaceholder("value.properties")

===========================================================

@Component, @Repository, @Service, @Bean, @SpringApplicationContext

@Autowired

public class SomeClass {
	@Autowired
	private OtherClass clazz;

	@Autowired
	public SomeClass(AClass ac){

	}	


	public void setdata(@Autowired DBUtil util) {

	}
}
\==================================================================\

	Spring Data JPA
	---------------

	Spring boot configures out of the box 
		1) Hibernate as ORM framework
		2) Hikari dataSource for database connection pooling
		3) Query methods support

	Hibernat as ORM framework
	Object Relational Mapping

	em.save(p); // insert into ...

	em.find(Product.class, 25); // select .... where id = 25


	em.merge(p); // update

	em.delete(p); // delete


	JPA ==> Specification for ORM frameworks

	ORM: Hibernate, Toplink, Kodo, OpenJPA, ...

	Oracle ==> JPA [ rules for ORM]


	=================

	1) EntityManager is a wrapper for database connection
		==> CRUD operations on tables [ product, custoemr., order ]of database

	2) ORM provider ==> Hibernate, TopLink,

	3) EntityManagerFactory
		==> factory class to crete EntityManager
		==> makes use of avaialble ORM provider + DB conection pool
		==> PersistenceContext
				==> env to manage entities ==> sync with database

	=========
	
	Without Spring Boot

	@Configuration		
	public class DbConfig {

		@Bean
		public HikariDataSource dataSource() {
			HikariDataSource ds = new HikariDataSource();
			ds.setUrl(...);
			ds.setUser(,,);
			ds.setPassword(..);
			ds.setPoolSize(..);
			...
		}

		@Bean
		public LocalContainerEntityManagerFactoyBean emf(DataSource ds) {
			LocalContainerEntityManagerFactoyBean emf = new LocalContainerEntityManagerFactoyBean();
			emf.setProvider(new HibernateVendor());
			emf.setDataSource(ds);
			...
		}
	}
 
	@Repository
	public class MyRepo {

		@PersistenceContext
		EntityManager em;

		public void addProduct(Product p) {
			em.save(p);
		}

	}

	=========================

	Spring Boot Data JPA
		==> HikariDataSource and Hibernate is configured
		==> application.properites ==> url, user, pwd, ...


		1) spring.jpa.hibernate.ddl-auto=create
		drop existing tables and re-create the tables
		use this for testing

		2) spring.jpa.hibernate.ddl-auto=update
			use tables if exists else create table [orm]

		3) spring.jpa.hibernate.ddl-auto=validate
			don't create tables use existing table


	==========

	Without Spring boot Data JPA

	public interface ProductDao {
		void addProduct(Product p);
	}

	@Repository
	public class ProductDaoJpaImpl implements PrpductDao {
		@PersistenceContext
		EnityManager em;

		public void addProduct(Product p){
			em.persist(p);
		}	
	}

	======

	With Spring DATA JPA

	public interface ProductDao extends JPARepository<Product, Integer> {

	}

	generates on the fly implmentation classes and manged by container

	this gives basic operations for CRUD [ methods]


	Product p = productDao.getOne(id); // lazy loading

	p will be proxy object

	p.getName(); // at his point actual product object is created

	p will be proxy not actual


	==========

	JPQL ==> Java Persistence Query language
	use class and fields for query and not SQL


	public interface ProductDao extends JpaRepository<Product, Integer> {

	List<Product> findByDiscontinued(int avail); // select * from products where category = ?
	
	// List<Product> findByCategoryAndDiscontinued(String category, int avail); 
	// select * from products where category = ? and discontinued = ?
	
	@Query("SELECT p FROM Product p where p.listPrice >= :lr and p.listPrice <= :up")
	List<Product> getProductsByRange(@Param("lr") double lower, @Param("up") double upper);
	
}


package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductDao;
import com.example.demo.entity.Product;

@Service
public class OrderService {
	@Autowired
	private ProductDao productDao;

	public List<Product> getProducts() {
		return productDao.findAll();
	}

	public Product addProduct(Product p) {
		return productDao.save(p);
	}

	public Product getProductById(int id) {
		return productDao.findById(id).get();
	}

	public List<Product> getProductsByDiscontinued(int avail) {
		return productDao.findByDiscontinued(avail);
	}

	public List<Product>  getProductsByRange(double lower, double upper) {
		return productDao.getProductsByRange( lower,  upper);
	}
}


package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;

@SpringBootApplication
public class SpringdataBaseApplication implements CommandLineRunner {

	@Autowired
	private OrderService service;

	public static void main(String[] args) {
		SpringApplication.run(SpringdataBaseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// code executes after Spring container [ Applicationcontext is created ]
//		List<Product> products = service.getProducts();
//		List<Product> products = service.getProductsByDiscontinued(1);
		List<Product> products = service.getProductsByRange(10.0, 20.0);
		for (Product p : products) {
			System.out.println(p.getProductName() + ", " + p.getListPrice() + "," + p.getDiscontinued());
		}
	}

}


