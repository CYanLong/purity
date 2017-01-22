package com.purity.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.purity.mvc.exception.ContainerInitializeException;

public class ReflectUtil {
	
	
	private static final Logger LOGGER = Logger.getLogger(ReflectUtil.class.getName());
	
	
	/**
	 * 使用clazz的无参构造器创建对象.
	 * @param clazz
	 * @return
	 */
	public static Object newInstance(Class<?> clazz){
		try {
			Constructor<?> cons = clazz.getConstructor();
			return cons.newInstance();
		} catch (NoSuchMethodException | SecurityException | 
				InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Field> getFieldWithAnnotation(Class<?> clazz, 
			Class<? extends Annotation> annotation){
		List<Field> res = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields){
			if (field.isAnnotationPresent(annotation))
				res.add(field);
		}		
		return res;
	}
	
	public static Object invokeMethod(Object bean, Method method,
			Object... args){
		try {
			return method.invoke(bean, args);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static Class<? extends Annotation> onlyAnnotation(Method clazz, 
			Class<? extends Annotation>[] annotations ){
		if (clazz == null ||annotations == null)
			throw new NullPointerException();
		int count = 0, index = 0;
		for (int i = 0; i < annotations.length; i++){
			Annotation anno = clazz.getAnnotation(annotations[i]);
			if (anno != null){
				count++;
				index = i;
			}
		}
		if (count > 1){
			LOGGER.log(Level.SEVERE, "Incompatible Annotation in method: " + clazz, 
					new ContainerInitializeException("Incompatible Annotation"));
		}
		
		return count == 0 ? null : annotations[index];
	}
	
	public static void main(String[] args){
		
	}
	
	public static Method findMethod(String targetName, Object controller) {
		if (targetName == null || controller == null)
			throw new NullPointerException();
		
		Method[] methods = controller.getClass().getMethods();
		if (methods != null){
			for (Method method : methods){
				if (targetName.equals(method.getName()))
					return method;
			}
		}
		return null;//not found.
	}
	
}
