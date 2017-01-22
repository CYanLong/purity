package com.purity.mvc.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.purity.mvc.annotation.Controller;
import com.purity.mvc.param.ParamWrap;

@Controller
public class RouteHandler {
	
	private String[] path;
	
	private Method action;
	
	private Object controller;
		
	/* 此handler方法的所有参数 
	 * 此参数顺序与方法实际参数顺序一致.
	 * */
	private ParamWrap[] param;
	
	private Map<String, String> pathVar = new HashMap<String, String>();
	
	/*支持的 http method */
	private String[] httpMethods;
			
	private Class<? extends Annotation> responseType;
	
	public RouteHandler() {
		super();
	}	
	
	public RouteHandler(String path, Method action, Object controller) {
		super();
		this.action = action;
		this.controller = controller;
	}
	
	public ParamWrap[] getParam() {
		return param;
	}
	
	public RouteHandler setParam(ParamWrap[] param) {
		this.param = param;
		return this;
	}

	public String[] getPath() {
		return path;
	}

	public RouteHandler setPath(String[] path) {
		this.path = path;
		return this;
	}

	public Method getAction() {
		return action;
	}

	public RouteHandler setAction(Method action) {
		this.action = action;
		return this;
	}

	public Object getController() {
		return controller;
	}

	public RouteHandler setController(Object controller) {
		this.controller = controller;
		return this;
	}

	public String[] getHttpMethods() {
		return httpMethods;
	}

	public RouteHandler setHttpMethods(String[] httpMethods) {
		this.httpMethods = httpMethods;
		return this;
	}

	public Class<? extends Annotation> getResponseType() {
		return responseType;
	}

	public void setResponseType(Class<? extends Annotation> responseType) {
		this.responseType = responseType;
	}

	public String getPathValue(String pathVar2) {
		return pathVar.get(pathVar2);
	}

	public void setPathVar(String key, String value) {
		pathVar.put(key, value);
	}

	public void setPathParam(Map<String, String> pathParam) {
		this.pathVar = pathParam;
	}
	
	
	
}
