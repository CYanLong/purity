package com.purity.mvc.route;

import java.util.ArrayList;
import java.util.List;

public class Routers {
	
	private List<RouteHandler> routes = new ArrayList<RouteHandler>();
	
	public void addRoute(List<RouteHandler> routes){
		routes.addAll(routes);
	}
	
	public void addRoute(RouteHandler route){
		routes.add(route);
	}
	
	public void removeRoute(RouteHandler route){
		routes.remove(route);
	}
	
	public List<RouteHandler> getRoutes(){
		return routes;
	}
	
	public void setRoutes(List<RouteHandler> routes){
		this.routes = routes;
	}
	
}
