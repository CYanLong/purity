package com.purity.ioc.test;

import com.purity.ioc.annotation.Inject;
import com.purity.mvc.annotation.Controller;

@Controller
public class Controller1 {
	
	@Inject
	private Service1 service1;
	
	@Inject
	private Service2  service2;


}
