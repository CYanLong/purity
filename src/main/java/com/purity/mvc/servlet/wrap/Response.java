package com.purity.mvc.servlet.wrap;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class Response {
	
	private HttpServletResponse raw;
	
	private Class<? extends Annotation> responseType;
	
	public Response(HttpServletResponse httpResponse) {
		this.raw = httpResponse;
	}

	public void text(String text){
		raw.setContentType("text/plan;charset=UTF-8");
		this.print(text);
	}

	private void print(String text) {
		try {
			OutputStream outStream = raw.getOutputStream();
			outStream.write(text.getBytes());
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cookie(String name, String value){
		Cookie cookie = new Cookie(name, value);
		raw.addCookie(cookie);
	}
	
	public void redirect(String location){
		try {
			raw.sendRedirect(location);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HttpServletResponse getRaw() {
		return raw;
	}

	public Class<? extends Annotation> getResponseType() {
		return responseType;
	}

	public void setResponseType(Class<? extends Annotation> responseType) {
		this.responseType = responseType;
	}
	
	
	
}
