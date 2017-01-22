package com.purity.mvc;

import com.purity.mvc.annotation.Controller;
import com.purity.mvc.route.RouteHandler;
import com.purity.mvc.route.Routers;

@Controller
final class Purity {
	
	private Routers routers;
	
			
	private Purity(){
		routers = new Routers();
	}
	
	private static class PurityHolder {
		private static Purity ME = new Purity();
	}
	
	public static Purity me() {
		return PurityHolder.ME;
	}
	
	public Routers getRouters() {
		return routers;
	}
	
	public void addRoute(RouteHandler route){
		routers.addRoute(route);
	}

}
