package com.purity.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

public class PathUtil {

	private static final String SLASH = "/";
	public static final String VAR_REGEXP = null;
	public static final String VAR_REPLACE = null;

	public static String getRelativePath(HttpServletRequest request) {
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		
		path = path.substring(contextPath.length());
		
		if (!path.startsWith(SLASH)){
			path = SLASH + path;
		}
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String fixPath(String path) {
		if (path == null)
			return "/";
		
		if (!path.startsWith("/")){
			path = "/" + path;
		}
		if (path.length() > 1 && path.endsWith("/")){
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	
}
