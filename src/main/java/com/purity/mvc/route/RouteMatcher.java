package com.purity.mvc.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.purity.mvc.exception.RouteMatchException;
import com.purity.mvc.servlet.wrap.Request;

public class RouteMatcher {
	
	private List<RouteHandler> routers;
	
	private static final Logger LOGGER = Logger.getLogger(RouteMatcher.class.getName());
	
	public RouteMatcher(List<RouteHandler> routers){
		this.routers = routers;
	}
	
	public void setRoutes(List<RouteHandler> routers){
		this.routers = routers;
	}
		
	/**
	 * 对request进行路由匹配.
	 * 1.URL的匹配.
	 * 2.http method的匹配.
	 * 3.query parameter的匹配.
	 * @param request
	 * @return
	 */
	public RouteHandler matchRoute(Request request){
		List<RouteHandler> res = 
				matchByMethod(matchByUri(routers, request), request);
		if (res.isEmpty())
			throw new RouteMatchException("not match route for request"); // not. 
		if (res.size() > 1)
			throw new RouteMatchException("matched ambiguity route");
		return res.get(0);
	}
	
	public List<RouteHandler> matchByUri(List<RouteHandler> routers, Request request) {
		String uri = request.getUri();
		List<RouteHandler> res = new ArrayList<RouteHandler>();
		for (RouteHandler route : routers){
			String[] paths = route.getPath();
			for (String path : paths){
				if (uri.equals(path))
					res.add(route);
				else {
					Map<String, String> pathParam = varPathMatch(uri, path);
					if (pathParam != null){
						LOGGER.log(Level.INFO, "match: path parameter: {0}", pathParam);
						route.setPathParam(pathParam);
						res.add(route);
					}
				}
			}
		}
		return res;
	}
	
	private Map<String, String> varPathMatch(String uri, String varPath) {
		String[] uriArr = uri.split("/");
		String[] varArr = varPath.split("/");
		
		if (uriArr.length != varArr.length)
			return null;
		
		Map<String, String> pathParam = new HashMap<String, String>();
		for (int i = 0; i < uriArr.length; i++){
			if (!uriArr[i].equals(varArr[i])){
				if (varArr[i].startsWith(":"))
					pathParam.put(varArr[i].substring(1, varArr[i].length()), 
							uriArr[i]);
				else
					return null;
			}
		}
		return pathParam;
		
	}

	public List<RouteHandler> matchByMethod(List<RouteHandler> routes, Request request){
		List<RouteHandler> res = new ArrayList<RouteHandler>();
		String method = request.method();
		for (RouteHandler route : routes){
			for (String m : route.getHttpMethods()){
				if (method.equals(m)){
					res.add(route);
					break;
				}
			}
		}
		return res;
	}
}

