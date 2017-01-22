package com.purity.mvc.param;

import java.lang.reflect.Parameter;

public class QueryParamWrap extends ParamWrap{
	
	private String queryParam;
	
	private boolean required;
	
	private Parameter parameter;
	
	public QueryParamWrap(String key, boolean required, 
			Parameter parameter){
		this.queryParam = key;
		this.required = required;
		this.parameter = parameter;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	
	public String toString(){
		return "<query param: " + queryParam + 
				" required: " + required + 
				" type: " + parameter.getType() + ">";
	}
}
