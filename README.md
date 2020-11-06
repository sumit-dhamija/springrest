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

========================================================================================

	
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String productCode;
	private String productName;
	private String description;
	private double standardCost;
	private double listPrice;
	private int targetLevel;
	private int reorderLevel;
	private int minimumReorderQuantity;
	private String quantityPerUnit;
	private int discontinued;

}

// return type is an entity
public interface ProductDao extends JpaRepository<Product, Integer> {
	List<Product> findByDiscontinued(int avail);
}

// scalar values
public interface ProductDao extends JpaRepository<Product, Integer> {
	@Query("SELECT p.productName, p.listPrice from Product p")
	List<Object[]> getNameAndPrice();
}


@Entity
@Table(name="customers")
public class Customer {
		@Id email; firstName; lastName;
}

@Entity
@Table(name="orders")
public Order {
	id; orderDate; total; 

	@ManyToOne()
	@JoinColumn("customer_fk")
	Customer customer;
}

customers
email 				|   first_name 		| last_name
a@adobe.com 			Askok 				Kumar

orders
id |order_date | total | customer_fk
12  04-11-2020  	545  a@adobe.com

74 02-10-2020   74545  a@adobe.com

class ReportDTO {
	email;
	order_date
	total
}



public OrderDao extends JPARepository<Order, Integer> {
 @Query("select c.email, o.orderDate, o.total from Order o inner join o.customer c")
  List<Object[]> getReport();
}


public OrderDao extends JPARepository<Order, Integer> {
 @Query("select new com.example.demo.ReportDTO(c.email, o.orderDate, o.total) from Order o inner join o.customer c")
  List<ReportDTO> getReport();
}

=======================================================================

public interface ProductDao extends JpaRepository<Product, Integer> {
	
	@Query("update Product product set product.listPrice = :p.listPrice where product.id = :p.id")
	@Modifying
	void updateProduct(@Param("p") Product p);
}

==========================================================


@Autowired
	private ProductDao productDao;
	
	@Override
	public void run(String... args) throws Exception {
		
		Pageable pageable = PageRequest.of(0, 3);
		
		Page<Product> page = productDao.findAll(pageable);
		
		System.out.println("Page :" + page.getNumber());
		System.out.println("# Pages :" + page.getTotalPages());
		
		List<Product> products = page.getContent();
		for(Product p : products) {
			System.out.println(p.getProductName() + ", " + p.getListPrice());
		}
	}

===============================

Pageable pageable = PageRequest.of(0, 3, Sort.by("listPrice").ascending());
		
		Page<Product> page = productDao.findAll(pageable);
		
		System.out.println("Page :" + page.getNumber());
		System.out.println("# Pages :" + page.getTotalPages());
		
		List<Product> products = page.getContent();
		for(Product p : products) {
			System.out.println(p.getProductName() + ", " + p.getListPrice());
		}

===================


Docker Desktop
docker pull redis
 
## step2 - Running the container
docker run -d -p 6379:6379 --name my-redis redis


UI with NodeJS for Redis:
$ npm install -g redis-commander
$ redis-commander
======================================

Day 2
-----
	Spring Framework ==> Spring boot
	==> Metadata ==> XML or Annotation

	class level ==> @Component; @Configuration; @Repository; @Service; 
	@Controller ==> MVC pattern for web application [ JSP, thymeleaf]
	@RestController

	@Bean at method level ==> we need to create objects and pass it to Spring container making it eligible for DI

	@SpringBootApplication ==> @configuration + @EnableAutoConfiguration [ scan jars and instantiate] + @ComponentScan [ for creating project specific beans]
	----------------

	CrudRepository [ Iterator<T> findAll()==> JpaRepository [ List<T> findAll()  + Pagination and sorting support]

	Query creation methods
		findBy queryBy countBy getBy 

		entity has name and email properties;
		Example: findByNameAndEmail(String name, String email);

		findByCategoryOrPrice(String category, double price);

		findByPriceBetween(double lower, double upper);


		findByCategoryIN(List<string> categories);


		@Query("JPQL query where filed = :p1")
		List<T> details(@Param("p1") String str);

		Projections:
		1) Scalar values

		@Query("select p.name, p.price from Product p")
		List<Object[]> getNameAndPrice();

		Object[0] will be name
		Object[1] will be price


		2) Create your DTO

		public class ReportDTO { email, orderDate, total}

		@Query("select c.email, o.orderDate, o.total from Order o left outer join o.customer o")
		List<Object[]> getReport();


		@Query("select new com.adobe.dto.ReportDTO(c.email, o.orderDate, o.total) from Order o left outer join o.customer o")
		List<ReportDTO> getReport();

----------------------------------------------------------------------------
	Spring Data Envers ==> Audited
	------------------------------


	NamedQueries ==> Predified queries at entity level
	@NamedQueries({
			@NamedQuery(name="MyEntity.detailReport", query ="......"),
			@NamedQuery(name="MyEntity.otherDetailReport", query ="......")
		})
	public class MyEntity{}

	interface MyEntityDao extends JPARepositry<...> {
		List<Myentity> detailReport();
	}
-----------------------------------------------------
	Entity Graph:

	Lazy and Eager fetching

	public class Order {

		@OneToMany
		@JoinColumn(name="order_id")
		List<Item> items = new ArrayList<>();

		@ManyToOne
		@JoinColumn(name="customer_id")
		Customer cutomer;
	}

	Assuming Customer and Item entities are written

	1) By default OneToMany is LAZY loading and ManyToOne will be EAGER fetching

	interface OrderDao extends JpaRepository<ORder, Integer> {

	}

	in client:
		List<Order> orders = orderDao.findAll();

			select * from orders;
			select * from customers c join on orders o where c.id = o.customer_id;

		When I traverse thro orders I can alos get customer info; even if connection to DB is lost

		List<Item> items = order.getItems(); // if connection is lost we get LazyInitializationException

		If Connection is still there:

		select * from items where orderid= ?

		this results in N + 1 issue

		if there are 100 orders; total hit to DB is 100 [select * from items where orderid = ?] + 1 [ select * from orders]

		-------------


		@OneToMany(fetch=FetchType.EAGER)
		@JoinColumn(name="order_id")
		List<Item> items = new ArrayList<>();

		while pulling orders it joins on items and single join query is triggerd [ 1 hit to DB]

		a) If connection is lost also
			we can get items
			List<Order> orders = orderDao.findAll();
		b) no N + 1 problem

		--------------
		:companyWithDepartmentsGraph

		 select
        company0_.id as id1_2_0_,
        company0_.name as name2_2_0_,
        department1_.company_id as company_3_3_1_,
        department1_.id as id1_3_1_,
        department1_.id as id1_3_2_,
        department1_.company_id as company_3_3_2_,
        department1_.name as name2_3_2_ 
    from
        company company0_ 
    left outer join
        department department1_ 
            on company0_.id=department1_.company_id 
    where
        company0_.id=?


        =========

        companyWithDepartmentsAndEmployeesGraph

        select
        company0_.id as id1_2_0_,
        company0_.name as name2_2_0_,
        department1_.company_id as company_3_3_1_,
        department1_.id as id1_3_1_,
        department1_.id as id1_3_2_,
        department1_.company_id as company_3_3_2_,
        department1_.name as name2_3_2_,
        employees2_.department_id as departme5_4_3_,
        employees2_.id as id1_4_3_,
        employees2_.id as id1_4_4_,
        employees2_.address_id as address_4_4_4_,
        employees2_.department_id as departme5_4_4_,
        employees2_.name as name2_4_4_,
        employees2_.surname as surname3_4_4_ 
    from
        company company0_ 
    left outer join
        department department1_ 
            on company0_.id=department1_.company_id 
    left outer join
        employee employees2_ 
            on department1_.id=employees2_.department_id 
    where
        company0_.id=?
=====================================================================================================================

Specification Example and redisCache 
------------------------------------

15 min break

=======================

Select * from products where price > 100; // key = price; value = 100 operation is GREATER_THAN

Select * from products where price < 100;

Select * from products where name like %A%;


=============================================

For enabling caching in Spring boot application:
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-cache</artifactId>
</dependency>

This uses InMemoryMapped Caching

===
CQRS

Caching implementation using Redis:
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
</dependency>

application.properties:
spring.redis.database=0
spring.redis.port=6379
spring.redis.host=127.0.0.1
spring.redis.lettuce.pool.min-idle=5
spring.redis.lettuce.pool.max-idle=10
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=1ms
spring.redis.lettuce.shutdown-timeout=100ms


@CacheConfig(cacheNames = "books")
public class SimpleBookRepository implements BookRepository {
	static List<Book> books = Arrays.asList(new Book("isbn-1234", "First Book"), new Book("isbn-4567", "Second Book"), new Book("isbn-4567", "Second Book") );
	
	@Override
	@Cacheable( key="#isbn") // books::isbn-1234: returned book will be value


==========

redis> keys *
redis> get books::isbn-4567

========================================================================================================
install: POSTMAN => for testing [REST client]

RESTful Web Services
====================
	Representational State Transfer

	A Resource resides on server [ database, files, tangilble things]

	Resoure is served to client in variaous represention [ XML, JSON, CSV, ...]

	REST is stateless ==> HTTP

	uses plural Nouns for REsource identification and HTTP verbs for actions [ CRUD ]

1)
	GET
	http://server/api/products

	get all products	

2) use path parameter for ID
	GET
	http://server/api/products/4

	get products where id = 4

	or to get sub resource

	GET
	http://server/api/customers/24/orders

	get all orders of customer whose id is 24

3) use Query parameter for filtering

	GET
	http://server/api/products?category=mobile

	get all mobile products

	http://server/api/products?page=0&size=10

=============

4) 	POST
	http://server/api/products

	client sends product as payload which needs to be added to products resource


5) 	PUT
	http://server/api/products/5

	client sends product as payload which needs to be update to product whose id is 5

6) DELETE	
	http://server/api/products/5
	delete a product whose id is 5


CRUD => CREATE READ UPDATE DELETE ==> POST GET PUT/PATCH DELETE [ HTTP Methods]

=========================================================================================

	RESTful ==> HTTP protocol

	HTTP header:

	Accept: application/json

	This header is sent by client requesting for JSON data from server

	Content-Type:text/xml
	This header specified by client is to inform the server that payload is of type "xml"
	B2C or B2B


	vanilla XML / JSON / protobuf
================================================

Spring Boot RestFul Web Services
spring-boot-web-starter as dependency
	==> ContentNegotionationHandler
		==> APIs for converting data to Representation
		==> JSON ==> Jackson library
		Jettison; GSON; Moxy ==> @Bean

		Product p = new Product(....);

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(browserStream, p ); ==> Product to JSON

	==> tomcat as web engine / server is added

==============================================================

	artifactId: orderapp
	groupId: com.adobe.prj
	package: com.adobe.prj
		Dependencies ==> Actuator; h2; mysql; jpa; web


=============

	copy Product, Customer, Item and Order entity classes into com.adobe.prj.entity

	cascade = CascadeType.ALL

	Order o = new Order();
	o has 3 items

	Without Cascade:

	save(o);
	save(i1);save(i3);save(i3);

	---

	delete(i1); delete(i2); delete(i3)
	delete(o);

	With Cascade:

	save(o); saving an order will save all items

	delete(o); deletes items also

	OrderDao; Don't need ItemDao

===============

	@Transactional for Declarative Transaction Management

	Programatic Tracnsaction

	public void accountTransaction(Account acc1, Account acc2, double amt) {
		Transaction tx = em.beginTransaction();
		logger.log("begin")
		try {
			logger.log("update acc1")
			em.update(acc1);
			logger.log("update acc2")
			em.update(acc2);
			Transact t = new Tranasct(..);
			em.save(t);
			tx.commit();
		} catch(DataAccessException ex) {
			tx.rollback();
		}
	}

	DeclarativeTranaction:

	@Transactional
	public void accountTransaction(Account acc1, Account acc2, double amt) {
			em.update(acc1);
			em.update(acc2);
			Transact t = new Tranasct(..);
			em.save(t);
	}
===================

@ResponseBody List<Product> 

	@ResponseBody: converts List<Product> to representation [ json / protobuf / xml] based on "accept" header
	checks ContentNegotionation Handler which uses HttpMessageConvertor to convert

@RequestBody Product p
	based on "content-type" header
	converts payload to product

================

@Transactional
	public Order addOrder(Order o) {
		List<Item> items = o.getItems();
		for (Item i : items) {
			Product p = productDao.findById(i.getProduct().getId()).get();
			if (i.getQty() > p.getQuantity())
				throw new RuntimeException();
			p.setQuantity(p.getQuantity() - i.getQty()); // dirty check happens and updates
		}
		return orderDao.save(o);
}

orderDao.save(o); ==> insert into orders also #n insert into items ==> CascadeType.ALL

===============

POST http://localhost:8080/api/orders

{
	"total" : 81000.00,
	"customer": {
		"email": "b@adobe.com"
	},
	"items" :[
		{
			"product": {"id": 2},
			"qty" : 3,
			"amount": 40.00
		},
		{
			"product": {"id": 1},
			"qty" : 1,
			"amount": 80000.00
		}	

	]
}

{
    "total":200,
    "customer":{
        "email":"a@adobe.com"
    },
    "items":[
        {
        "product":{"id":2},
        "qty" : 2,
        "amount" : 20.00
        },
        {
        "product":{"id":1},
        "qty" : 5,
        "amount" : 50000.00
        }
    ]
}

===========================================
 
 	@GetMapping()
	public @ResponseBody List<Product> getProducts(@) {
		return service.getAllProducts();
	}
==================================================

Spring has support for Aspect Oriented Programming [AOP]
	==
	Aspect: a concern which is not a part of main logic, but can be used along with main logic
		Examples: Logging, Transaction, ExceptionHandling, Security
		These concerns cuts results in lots of code tangling and scattering
	JoinPoint: a place where ascpect is weaved
		==> Method or Exception
	Advice: Before, After, Around, Throws
=========================

public RestTemplate restTemplate;
     
    @Before
    public void setUp() 
    {
        restTemplate = new RestTemplate(getClientHttpRequestFactory());
    }
     
    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() 
    {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                          = new HttpComponentsClientHttpRequestFactory();
         
        clientHttpRequestFactory.setHttpClient(httpClient());
              
        return clientHttpRequestFactory;
    }
     
    private HttpClient httpClient() 
    {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
 
        credentialsProvider.setCredentials(AuthScope.ANY, 
                        new UsernamePasswordCredentials("admin", "password"));
 
        HttpClient client = HttpClientBuilder
                                .create()
                                .setDefaultCredentialsProvider(credentialsProvider)
                                .build();
        return client;
    }
=========================

docker run -d --name=prometheus -p 9090:9090 -v C:\prometheus\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml

@Bean
    @ConditionalOnMissingBean(HttpTraceRepository.class)
    public InMemoryHttpTraceRepository traceRepository() {
        return new InMemoryHttpTraceRepository();
    }

<!-- Swagger -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.7.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.7.0</version>
		</dependency>

		<!-- Swagger end -->
			






