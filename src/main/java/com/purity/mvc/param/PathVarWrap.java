package com.purity.mvc.param;

public class PathVarWrap extends ParamWrap{
	
	private String pathVar;
		
	public PathVarWrap(String pathVar) {
		super();
		this.pathVar = pathVar;
	}

	public String getPathVar() {
		return pathVar;
	}

	public void setPathVar(String pathVar) {
		this.pathVar = pathVar;
	}
	
	
	public String toString(){
		return "<pathVar: " + pathVar + ">";
	}
}
