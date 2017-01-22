package com.purity.ioc.test;

import com.purity.PurityConfig;
import com.purity.ioc.IocConfigResolve;
import com.purity.mvc.PurityFilter;

public class ResolveTest {

	
	public static void testResolve(){
		PurityConfig.controllerPackage = new String[]{"com.purity.ioc.test"};
		PurityConfig.servicePackage = new String[]{"com.purity.ioc.test"};
		
		IocConfigResolve.resolve();
		PurityFilter filter = new PurityFilter();
		filter.inject();
	}
	
	public static void main(String[] args){
//		ResolveTest test = new ResolveTest();
		testResolve();
	}
	
}
