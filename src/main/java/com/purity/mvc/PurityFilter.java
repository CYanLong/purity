package com.purity.mvc;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.purity.Bootstrap;
import com.purity.PurityConfig;
import com.purity.ioc.IocConfigResolve;
import com.purity.ioc.PurityContainer;
import com.purity.ioc.annotation.Inject;
import com.purity.mvc.annotation.Json;
import com.purity.mvc.annotation.Redirect;
import com.purity.mvc.annotation.Render;
import com.purity.mvc.exception.RenderFailException;
import com.purity.mvc.exception.RouteMatchException;
import com.purity.mvc.json.JsonParse;
import com.purity.mvc.param.ModelMapWrap;
import com.purity.mvc.param.ParamWrap;
import com.purity.mvc.param.PathVarWrap;
import com.purity.mvc.param.QueryParamWrap;
import com.purity.mvc.param.RequestParamWrap;
import com.purity.mvc.param.ResponseParamWrap;
import com.purity.mvc.param.UnknowParam;
import com.purity.mvc.route.RouteHandler;
import com.purity.mvc.route.RouteMatcher;
import com.purity.mvc.route.Routers;
import com.purity.mvc.servlet.wrap.Request;
import com.purity.mvc.servlet.wrap.Response;
import com.purity.util.ReflectUtil;


public class PurityFilter implements Filter{

	private static final Logger LOGGER = Logger.getLogger(PurityFilter.class.getName());
	
	private RouteMatcher routeMatcher = 
			new RouteMatcher(new ArrayList<RouteHandler>());
	
//	private ServletContext servletContext;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		//加载用户回调代码并执行.
		String className = filterConfig.getInitParameter("bootstrap");
		
		if (className == null){
			LOGGER.log(Level.SEVERE, "Bootstrap ClassName null", 
					new NullPointerException());
		}
		Bootstrap bootstrap = this.getBootstrap(className);
		bootstrap.init(); //执行用户配置代码.
		
		IocConfigResolve.resolve(); //ioc container 相关的信息解析.
		inject();  //依赖注入.
		Purity purity = MVConfigResolve.resolve(); //mvc相关的信息解析.
		
		Routers routes = purity.getRouters();
		if (routes != null){
			routeMatcher.setRoutes(routes.getRoutes());
		}
	}
	
	/**
	 * 实现依赖注入.
	 */
	public void inject() {
		Set<Object> controllers = PurityContainer.ME.getControllers();
		PurityContainer container = PurityContainer.ME;
		for (Object obj : controllers){
			List<Field> fields = ReflectUtil.getFieldWithAnnotation(obj.getClass(),
					Inject.class);
			for (Field field : fields){
				Object targetObj = null;
				if ((targetObj = container.getBean(field.getType())) == null){
					//throw a Exception;
				}
				field.setAccessible(true);
				try {
					field.set(obj, targetObj);
					LOGGER.log(Level.INFO, "set dependence: {0} to {1}", 
							new Object[]{targetObj, field});
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private Bootstrap getBootstrap(String className) {
		if (null != className){
			try {
				Class<?> clazz = Class.forName(className);
				System.out.format("%s load %s", clazz.getClassLoader(), clazz.getName());
				return (Bootstrap)clazz.newInstance();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null; //throw exception.
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		if (isStaticResourceRequest(httpRequest)){
			LOGGER.log(Level.INFO, "static resource: {0}", httpRequest.getRequestURI());
			chain.doFilter(httpRequest, httpResponse);
			return ;
		}
		
		RouteHandler route = routeMatcher.matchRoute(new Request(httpRequest));
		if (route != null){
			handle(httpRequest, httpResponse, route);
		} else{
			LOGGER.log(Level.SEVERE, "not route match: {0}", new Exception());
			chain.doFilter(request, response);
		}
	}

	private boolean isStaticResourceRequest(HttpServletRequest httpRequest) {
		String[] staticSuffix = PurityConfig.staticResourceSuffix;
		String uri = httpRequest.getRequestURI();
		for (String suffix : staticSuffix){
			if (uri.endsWith(suffix))
				return true;
		}
		return false;
	}

	private void handle(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse, 
			RouteHandler route) {
		Request request = new Request(httpRequest);
		Response response = new Response(httpResponse);
		
		Object[] args = getArgs(response, route, request);
		LOGGER.log(Level.INFO, "match: controller: {0}, method: {1}", 
				new Object[]{route.getController(), route.getAction()});
		Object result = ReflectUtil.invokeMethod(route.getController(), route.getAction(), args);
		afterInvoke(request, response, route, result);
	}
	
	private void afterInvoke(Request request, Response response, 
			RouteHandler route, Object result) {
	
		Class<? extends Annotation> clazz = route.getResponseType();
		if (clazz == null){
			return ; //不需要处理
		}
		
		if (Json.class == clazz){
			try {
				response.getRaw().getWriter().write(JsonParse.parseObject(result));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (Render.class == clazz){
			if (!(result instanceof String))
				throw new RenderFailException("template name is not string");
			
			HttpServletRequest httpRequest = request.getRaw();
			ModelMap model = request.getModelMap();
			for (Map.Entry<String, Object> entry : model.entrySet()){
				httpRequest.setAttribute(entry.getKey(), entry.getValue());
			}
			String viewPath = PurityConfig.viewResourceLoaderLocation + 
					(String)result + PurityConfig.viewSuffix;
			LOGGER.log(Level.INFO, "render view: {0}", viewPath);
			try {
				httpRequest.getRequestDispatcher(viewPath).forward(httpRequest, response.getRaw());
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
			
		} else if (Redirect.class == clazz){
			String location = null;
			if (result instanceof String)
				location = (String)result;
			try {
				response.getRaw().sendRedirect(location);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Object[] getArgs(Response response, RouteHandler route, Request request) {
		ParamWrap[] params = route.getParam();
		Object[] args = new Object[params.length];
		for (int i = 0; i < params.length; i++){
			ParamWrap param = params[i];
			if (param instanceof RequestParamWrap)
				args[i] = request.getRaw();
			else if (param instanceof ResponseParamWrap)
				args[i] = response.getRaw();
			else if (param instanceof QueryParamWrap){
				QueryParamWrap queryParam = (QueryParamWrap) param;
				args[i] = request.queryParam(queryParam);
				if (args[i] == null && queryParam.isRequired())
					LOGGER.log(Level.SEVERE, "requried query param " + queryParam.getQueryParam() + ""
							+ " not found.", new RouteMatchException("required query param not found"));
			}else if (param instanceof PathVarWrap){
				String pathVar = ((PathVarWrap) param).getPathVar();
				String val = route.getPathValue(pathVar);
				if (val == null){
					LOGGER.log(Level.SEVERE, "path param not found", 
							new RouteMatchException("path param " + pathVar + " not found."));
				}
				args[i] = val;
			}else if (param instanceof ModelMapWrap){
				request.setModelMap(new ModelMap());
				args[i] = request.getModelMap();
			}else if (param instanceof UnknowParam){
				LOGGER.log(Level.WARNING, "unkown param: {0}.", param);
				args[i] = null;
			}
		}
		return args;
	}
	
		
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
