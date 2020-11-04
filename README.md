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


	