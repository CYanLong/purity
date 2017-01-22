package purity;

import java.lang.reflect.InvocationTargetException;

import com.purity.mvc.annotation.Forward;
import com.purity.mvc.annotation.Json;

public class ReflectTest {
	
	public void method1(int num){
		System.out.println("method1: " + num);
	}
	
	@Json
	@Forward
	public void method2(Integer num){
		System.out.println("method2: " + num);
	}
	
	
	public static void test() throws NoSuchMethodException, SecurityException{
//		Method method = ReflectTest.class.getMethod("method2", Integer.class);
//		Class<? extends Annotation> res = 
//				ReflectUtil.onlyAnnotation(method, Json.class, Redirect.class, Forward.class);
//		System.out.println(res.getName());
	}
	
	public static void main(String[] args) throws NoSuchMethodException, 
					SecurityException, InstantiationException,
					IllegalAccessException, IllegalArgumentException, 
					InvocationTargetException{
		test();
	}

}