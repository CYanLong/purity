package com.purity;

public final class PurityConfig {
	
	public static String[] controllerPackage;
	
	public static String[] servicePackage;
	
	public static String viewSuffix = ".jsp";
	
	public static String viewResourceLoaderLocation = "";
	
	/**
	 * 关于静态资源后缀名的配置. 默认为空.
	 */
	public static String[] staticResourceSuffix = new String[]{};
	
	public static boolean contains(String suffix){
		return true;
	}
	
	private static class PurityConfigHolder{
		private static PurityConfig ME = new PurityConfig();
	}
	
	public static PurityConfig me(){
		return PurityConfigHolder.ME;
	}
	private PurityConfig(){
	}
	
}
