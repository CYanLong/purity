package com.purity.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.purity.PurityConfig;
import com.purity.ioc.PurityContainer;
import com.purity.mvc.annotation.Controller;
import com.purity.mvc.annotation.Json;
import com.purity.mvc.annotation.PathVar;
import com.purity.mvc.annotation.QueryParam;
import com.purity.mvc.annotation.Redirect;
import com.purity.mvc.annotation.Render;
import com.purity.mvc.annotation.Route;
import com.purity.mvc.exception.ContainerInitializeException;
import com.purity.mvc.param.ModelMapWrap;
import com.purity.mvc.param.ParamWrap;
import com.purity.mvc.param.PathVarWrap;
import com.purity.mvc.param.QueryParamWrap;
import com.purity.mvc.param.RequestParamWrap;
import com.purity.mvc.param.ResponseParamWrap;
import com.purity.mvc.param.UnknowParam;
import com.purity.mvc.route.RouteHandler;
import com.purity.util.PackageScanner;
import com.purity.util.ReflectUtil;

public class MVConfigResolve {
	
	private static final Logger LOGGER = Logger.getLogger(MVConfigResolve.class.getName());

	@SuppressWarnings("unchecked")
	public static Purity resolve(){
		
		Purity purity = Purity.me();
		String[] controllerPackage = PurityConfig.controllerPackage;
		Map<Class<?>, List<Method>> classToMethods = 
				PackageScanner.getMethodsAnnotatedWith(controllerPackage, Controller.class, Route.class);
		for (Map.Entry<Class<?>, List<Method>> entry : classToMethods.entrySet()){
			for (Method method : entry.getValue()){
				RouteHandler route = new RouteHandler();
				route.setAction(method)
					.setHttpMethods(resolveHttpMethod(method))
					.setParam(resolveParam(method))
					.setPath(resolvePath(method))
					.setController(PurityContainer.ME.getBean(entry.getKey()))
					.setResponseType(ReflectUtil.onlyAnnotation(method, new Class[]{Json.class, Render.class,
							Redirect.class}));
				purity.addRoute(route);
				LOGGER.log(Level.INFO, "uri:{0}, method: {1}, controller: {2}, "
						+ "methodName: {3} parameter: {4}", 
						new Object[]{Arrays.toString(route.getPath()),
									Arrays.toString(route.getHttpMethods()),
									route.getController(),
									route.getAction().getName(),
									Arrays.toString(route.getParam())});
			}
		}
		return purity;
	}
	
	
	
	public static String[] resolvePath(Method method){
		Route[] route = method.getAnnotationsByType(Route.class);
		return route[0].path();
	}

	public static String[] resolveHttpMethod(Method method){
		Route[] route = method.getAnnotationsByType(Route.class);
		return route[0].methods();
	}
	
	/**
	 * 解析method方法的参数信息.
	 * @param method
	 * @return
	 */
	public static ParamWrap[] resolveParam(Method method){
		if (method == null)
			throw new NullPointerException();
		Parameter[] parameters = method.getParameters();
		int len = parameters.length;		
		ParamWrap[] params = new ParamWrap[len];
		
		for (int i = 0; i < len; i++){
			Parameter parameter = parameters[i];
			Class<?> clazz = parameter.getType();
			if (clazz == HttpServletRequest.class)
				params[i] = new RequestParamWrap();
			else if (clazz == HttpServletResponse.class)
				params[i] = new ResponseParamWrap();
			else if (clazz == ModelMap.class){
				params[i] = new ModelMapWrap();
			}
			else { //no-specify parameter. depend annotation
				QueryParam[] queryParam = 
						parameter.getAnnotationsByType(QueryParam.class);
				PathVar[] pathVar = parameter.getAnnotationsByType(PathVar.class);
				if (queryParam.length == 0 && pathVar.length == 0)
					params[i] = new UnknowParam();
				
				if (queryParam.length == 1 && pathVar.length == 1)
					LOGGER.log(Level.SEVERE, "incompatible annotation in param: " + parameter, 
							new ContainerInitializeException("incompatible annotation: "
							+ "" + queryParam[0].annotationType() + " and " + pathVar[0].annotationType()));//InCompatibleAnnotation
				
				if (queryParam.length == 1){
					params[i] = new QueryParamWrap(queryParam[0].value(), 
							queryParam[0].required(), parameter);
				} else if (pathVar.length == 1){
					params[i] = new PathVarWrap(pathVar[0].value());
				}
			}
			
		}
		return params;
	}

}
