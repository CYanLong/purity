package com.purity.test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogExceptionTest {

	static Logger LOGGER = Logger.getLogger(LogExceptionTest.class.getName());
	
	public LogExceptionTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args){
		int i = 0;
		if (i == 0){
			LOGGER.log(Level.SEVERE, "test Exception", new TestException("test Exception lalal"));
			System.out.println("lalaa");
		}
		System.out.println("ddd");
	}

}
