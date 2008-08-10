package com.ryanberdeen.routes.xml;

import java.util.HashMap;

class RouteBuilder implements Cloneable {
	public HashMap<String, String> metaParameters;
	public HashMap<String, String> parameterValues;
	public HashMap<String, String> defaultStaticParameterValues;
	public HashMap<String, String> parameterRegexes;
	
	public RouteBuilder() {
		initDefault();
	}
	
	public RouteBuilder(RouteBuilder that) {
		if (that != null) {
			metaParameters = new HashMap<String, String>(that.metaParameters);
			parameterValues = new HashMap<String, String>(that.parameterValues);
			defaultStaticParameterValues = new HashMap<String, String>(that.defaultStaticParameterValues);
			parameterRegexes = new HashMap<String, String>(that.parameterRegexes);
		}
		else {
			initDefault();
		}
	}
	
	private void initDefault() {
		metaParameters = new HashMap<String, String>();
		parameterValues = new HashMap<String, String>();
		defaultStaticParameterValues = new HashMap<String, String>();
		parameterRegexes = new HashMap<String, String>();
	}
	
	public String getMetaParameter(String name) {
		return metaParameters.get(name);
	}
	
	public String getMetaParameter(String name, String defaultValue) {
		String result = metaParameters.get(name);
		return result != null ? result : defaultValue;
	}
	
	@Override
	protected RouteBuilder clone() throws CloneNotSupportedException {
		return new RouteBuilder(this);
	}
}
