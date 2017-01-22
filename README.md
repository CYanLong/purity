### 基于Servlet 的MVC框架.
---

[PurityConfig 配置] PurityConfig 配置

<H4>快速开始</H4>
	
1、实现 `Bootstrap` 回调接口，实现init()方法。在 `init()` 方法中进行相关配置(No-XML)。 对 `PurityConfig` 不可实例化类的相关静态字段配置。


		public class App implements Bootstrap{

			@Override
			public void init() {
				PurityConfig.controllerPackage = 
					new String[]{"com.purity.sample.test"};
				PurityConfig.viewResourceLoaderLocation = "";
				PurityConfig.viewSuffix = ".vm";
				...									
			}
		}
 

2、`Web.xml` 配置。 在配置全局拦截时需设置初始化参数 `bootstrap` 信息。即告知实现了 `Bootstrap` 回调的配置类全限定类名。
	
		<filter>
			<filter-name>purityFilter</filter-name>
 			<filter-class>com.purity.mvc.PurityFilter</filter-class>
 			<init-param>
 				<param-name>bootstrap</param-name>
 				<param-value>com.purity-sample.App</param-value>
 			</init-param>
 		</filter>
 	
		<filter-mapping>
			<filter-name>purityFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
	
3、编写Controller。

		@Controller
		public class ControllerTest {
			
			@Route(path = "/user/:userId", methods = {"GET"})
			@Json
			public Map<String, String> method1(@PathVar("userId") String userId, 
					@QueryParam("queryKey") String queryKey){
				System.out.println(userId);
				System.out.println(queryKey);
				Map<String, String> data = new HashMap<String,String>();
				data.put("name", "cyanlong");
				data.put("birthday", "1996-09-22");
				return data;
			}
		}


4、 第三方依赖：
	
	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-databind</artifactId>
		<version>2.6.3</version>
	</dependency> 

[toc]<H4> PurityConfig 配置</H4>

1、Controller 类包配置，在Purity 容器初始化时只会在指定包下扫描标 `Controller` 的类。
		
		//必须配置.
		PurityConfig.controllerPackage = 
				new String[]{"com.purity.sample.controllerPack1", 
					"com.purity.sample.controllerPack2"};

2、视图配置。支持 `JSP` 和 `Velocity` 视图渲染。
	
		//视图资源位置。 默认为 webapp/ 下.
		PurityConfig.viewResourceLoaderLocation = "/WEB-INF/view/";
		//视图后缀. 默认为 .jsp
		PurityConfig.viewSuffix = ".vm";

若配置了 `Velocity`，需在 `web.xml` 中进行如下配置:
		
	<servlet>
        <servlet-name>velocity</servlet-name>
        <servlet-class>org.apache.velocity.tools.view.VelocityViewServlet</servlet-class>
	</servlet>

	 <servlet-mapping>
	 	<servlet-name>velocity</servlet-name>
	 	<url-pattern>*.vm</url-pattern>
	 </servlet-mapping>

 并且需要依赖：

	<dependency>
		<groupId>org.apache.velocity</groupId>
		<artifactId>velocity-tools</artifactId>
		<version>2.0</version>
		<exclusions>  
           <exclusion>  
               <groupId>javax.servlet</groupId>  
               <artifactId>servlet-api</artifactId>  
           </exclusion>  
   		 </exclusions>
	</dependency>

3、静态资源配置。 `PurityFilter` 将过滤指定后缀名的访问。
		
	PurityConfig.staticResourceSuffix = 
			new String[]{".html", ".jpg", ".css", ".js"};



[toc]<H4> MVC 相关注解。</H4>

1、`@Route` 路由定义：
	
	@Route(path = "/index", method = {"GET"})
	public void index(){
	}

2、`@QueryParam("queryKey")` HTTP查询参数获取:
	
	@Route(path = "/user", method = {"GET"})
	public void getUser(@QueryParam("userId") int userId){
		System.out.println(userId);
	}

3、`@PathVar("path")` 路径参数：
	
	@Route(path = "/user/:userId", method = {"POST"})
	public void addUser(@PathVar("userId") Integer userId){
		...
	}

4、直接声明形参 访问 `HttpServletRequest`, `HttpServletResponse`：

	@Route("/handler") //default GET method
	public void handler(HttpServletRequest request, 
				HttpServletResponse response){
		//可直接使用request, response.
	}


5、 `@Json` 返回Json：
	
	@Route("index")
	@Json
	public Map<String, String> method(){
		Map<String, String> data = new HashMap<String,String>();
		data.put("name", "cyanlong");
		data.put("birthday", "1996-09-22");
		return data;
	}

6、 `@Render` 视图转发： 可声明形参 `ModelMap` 并直接对其填充渲染数据。 
	
	@Route("index")
	@Render    	//返回值必须为String。
	public String method(ModelMap model){
		model.addAttribute("user", "cyanlong");
		return "index"; //default suffix is .jsp.
	}

7、 `@Redirect` HTTP请求重定向。
	
	@Route("index")
	@Redirect //must return String.
	public String method(){
		return "index2";	
	} 


	
