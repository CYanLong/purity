package com.purity.ioc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PurityContainer {
	
	/**
	 * Class 类型与beans实例对象的映射.
	 */
	private Map<Class<?>, Object> beans;
	
	private Set<Object> controllers;
	
	private Set<Object> services;
	
	private PurityContainer(){
		beans = new ConcurrentHashMap<Class<?>, Object>();
		controllers = new HashSet<Object>();
		services = new HashSet<Object>();
	}
	
	private static class SingleHolder{
		private static PurityContainer container = 
				new PurityContainer();
	}

	//single instance
	public static PurityContainer ME = SingleHolder.container;
	
	public void registService(Class<?> clazz, Object instance){
		if (clazz == null || instance == null)
			throw new NullPointerException();
		services.add(instance);
		beans.put(clazz, instance);
	}
	
	public void registController(Class<?> clazz, Object instance){
		if (clazz == null || instance == null)
			throw new NullPointerException();
		controllers.add(instance);
		beans.put(clazz, instance);
	}
	
	/**
	 * 根据类型找到对应的实例.
	 * @param clazz class类型.
  	 * @return 容器中对应的
	 */
	public <T> T getBean(Class<T> clazz){
		return clazz.cast(beans.get(clazz));
	}

	public Set<Object> getControllers() {
		return controllers;
	}

	public void setControllers(Set<Object> controllers) {
		this.controllers = controllers;
	}

	public Set<Object> getServices() {
		return services;
	}

	public void setServices(Set<Object> services) {
		this.services = services;
	}
		
}
