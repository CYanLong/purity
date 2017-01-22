package com.purity.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.purity.mvc.annotation.Route;



public class PackageScanner {
	
	
	/**
	 * 得到packageNames包下所有标注为typeAnnotation类和对应的标注为
	 * methodAnnotation的所有方法.
	 * @param packageNames
	 * @param typreAnnotation
	 * @param methodAnnotation
	 * @return
	 */
	public static Map<Class<?>, List<Method>> getMethodsAnnotatedWith(
			String[] packageNames, Class<? extends Annotation> typeAnnotation, 
			Class<? extends Annotation> methodAnnotation){
		if (packageNames == null || typeAnnotation == null || 
				methodAnnotation == null)
			throw new NullPointerException();
		
		Collection<Class<?>> classes = 
				getClassesAnnotatedWith(packageNames, typeAnnotation); 
		return getMethodsAnnotatedWith(classes, methodAnnotation);
	}
	

	/**
	 * 加载packages下所有标记为anno的类.
	 * @param packages
	 * @param anno
	 * @return
	 */
	public static Set<Class<?>> getClassesAnnotatedWith(String[] packages, 
			Class<? extends Annotation> annotation){
		Set<Class<?>> classes = getClasses(packages);
		Set<Class<?>> res = new HashSet<Class<?>>();
		for (Class<?> clazz : classes){
			for (Annotation anno : clazz.getDeclaredAnnotations()){
				if (anno.annotationType() == annotation){
					res.add(clazz);
					break;
				}
			}
		}	
		return res;
	}	
	
	
	/**
	 * 得到classes集合中所有类中有annotation标注的方法.
	 * 返回一个Map,是关于类和对应有annotation标注所有方法的映射map.
	 * @param classes
	 * @param annotation
	 * @return
	 */
	public static Map<Class<?>, List<Method>> getMethodsAnnotatedWith(
			Collection<Class<?>> classes, Class<? extends Annotation> annotation){
		if (classes == null || annotation == null)
			throw new NullPointerException();

		Map<Class<?>, List<Method>> controlEntry = new HashMap<Class<?>, List<Method>>();
		for (Class<?> clazz : classes){
			List<Method> methods = getMethodsAnnotatedWith(clazz, Route.class);
			controlEntry.put(clazz, methods);
		}
		return controlEntry;
	}	
	
	/**
	 * 得到类clazz中所有标注为annotation的方法.	
	 * @param clazz
	 * @param annotation
	 * @return
	 */
	public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, 
			Class<? extends Annotation> annotation){
		if (clazz == null)
			throw new NullPointerException();
		List<Method> methods = new ArrayList<Method>();
		for (Method method : clazz.getDeclaredMethods()){
			if (method.getAnnotation(annotation) != null){
				methods.add(method);
			}
		}
		return methods;	
	}
	
		
	/**
	 * 加载packageNames下的所有类.
	 * @param packageNames
	 * @return
	 */
	public static Set<Class<?>> getClasses(String[] packageNames){
		if (packageNames == null)
			throw new NullPointerException();
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (String packageName : packageNames){
			try {
				classes.addAll(getClasses(packageName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return classes;
	}
	
	public static Set<Class<?>> getClasses(String packageName) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()){
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (File directory : dirs){
			classes.addAll(findClass(directory, packageName));
		}
		
		return classes;
	}

	private static Collection<? extends Class<?>> findClass(File directory, String packageName) {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()){
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files){
			if (file.isFile() && file.getName().endsWith(".class")){
				try {
					String fileName = file.getName();
					classes.add(Class.forName(packageName + "." + fileName.substring(0, fileName.length() - 6)));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return classes;
	}
	
//	public static void main(String[] args) {
//		String packageName  = "com.cyl.purity";
//		ArrayList<String> packageNames = new ArrayList<String>();
//		packageNames.add(packageName);
//		packageNames.add("com.cyl.purity.route");
//		Set<Class<?>> classes;
//		classes  = PackageScanner.getClassesAnnotatedWith(packageNames, Controller.class);
//		System.out.println(classes);
//	}
}
