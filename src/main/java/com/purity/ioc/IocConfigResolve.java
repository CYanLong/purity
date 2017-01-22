package com.purity.ioc;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.purity.PurityConfig;
import com.purity.ioc.annotation.Service;
import com.purity.mvc.annotation.Controller;
import com.purity.util.PackageScanner;
import com.purity.util.ReflectUtil;

public class IocConfigResolve {
	
	private static final Logger LOGGER = Logger.getLogger(IocConfigResolve.class.getName());
	
	public static PurityContainer resolve(){
		PurityContainer purityContainer = PurityContainer.ME;
		String[] servicePackage = PurityConfig.servicePackage;
		String[] controllerPackage = PurityConfig.controllerPackage;
		
		if (servicePackage != null){
			Set<Class<?>> allServices = 
					PackageScanner.getClassesAnnotatedWith(servicePackage, Service.class);
			for (Class<?> clazz : allServices){
				Object bean = ReflectUtil.newInstance(clazz);
				purityContainer.registService(clazz, bean);
				LOGGER.log(Level.INFO, "regist service instance: {0}", bean);
			}
		}
		
		if (controllerPackage != null){
			Set<Class<?>> allControllers = 
					PackageScanner.getClassesAnnotatedWith(controllerPackage, Controller.class);
			for (Class<?> clazz : allControllers){
				Object bean = ReflectUtil.newInstance(clazz);
				purityContainer.registController(clazz, bean);
				LOGGER.log(Level.INFO, "regist controller instance: {0}", bean);
			}
		}
		return purityContainer;
	}	
		
}
