package com.purity.mvc.servlet.wrap;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.purity.mvc.ModelMap;
import com.purity.mvc.exception.RouteMatchException;
import com.purity.mvc.param.PathVarWrap;
import com.purity.mvc.param.QueryParamWrap;
import com.purity.util.PathUtil;

public class Request {
	
	private static final Logger LOGGER = Logger.getLogger(Request.class.getName());

	private HttpServletRequest raw;
	
	private String uri;
	
	private ModelMap modelMap;
	
	public Request(HttpServletRequest request){
		this.raw = request;
		uri = PathUtil.getRelativePath(raw);
	}
	
		
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setRaw(HttpServletRequest raw) {
		this.raw = raw;
	}

	public HttpServletRequest getRaw(){
		return this.raw;
	}
	
	public void attr(String name, Object value){
		raw.setAttribute(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T attr(String name){
		Object value = raw.getAttribute(name);
		if (null != value){
			return (T) value;
		}
		return null;
	}
	
	public String queryParam(String name){
		return raw.getParameter(name);
	}
	
	public Integer queryInt(String name){
		String val = raw.getParameter(name);
		if (null != null){
			return Integer.valueOf(val);
		}
		return null;
	}
	
	public String cookie(String name){
		Cookie[] cookies = raw.getCookies();
		if (null != cookies && cookies.length > 0){
			for (Cookie cookie : cookies){
				if (cookie.getName().equals(name)){
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public Object queryParam(QueryParamWrap queryParamWrap) {
		String value = queryParam(queryParamWrap.getQueryParam());
		if (value == null){ //not found
			return null;
		}
		Class<?> paramType = queryParamWrap.getParameter().getType();
		if (paramType == Byte.class || paramType == byte.class){
			return Byte.valueOf(value);
		}else if (paramType == Short.class || paramType == short.class){
			return Short.valueOf(value);
		}else if (paramType == Integer.class || paramType == int.class){
			return Integer.valueOf(value);
		}else if (paramType == Long.class || paramType == long.class){
			return Long.valueOf(value);
		}else if (paramType == Float.class || paramType == float.class){
			return Float.valueOf(value);
		}else if (paramType == Double.class || paramType == double.class){
			return Double.valueOf(value);
		}else if (paramType == Character.class || paramType == char.class){
			if (value.length() != 1)
				LOGGER.log(Level.SEVERE, "expect: ,", new RouteMatchException(""));
			return Character.valueOf(value.toCharArray()[0]);
		}else if (paramType == boolean.class){
			return Boolean.parseBoolean(value);
		}else if (paramType == String.class){
			return value;
		}
		return null;//
	}
	
	public String method(){
		return raw.getMethod();
	}

	public Object pathVar(PathVarWrap param) {
			
		return null;
	}


	public ModelMap getModelMap() {
		return modelMap;
	}


	public void setModelMap(ModelMap modelMap) {
		this.modelMap = modelMap;
	}
	
}
