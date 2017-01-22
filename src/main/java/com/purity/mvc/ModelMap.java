package com.purity.mvc;

import java.util.LinkedHashMap;

public class ModelMap extends LinkedHashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		
	public ModelMap addAttribute(String attributeName, Object attributeValue){
		super.put(attributeName, attributeValue);
		return this;
	}
	
	
}
