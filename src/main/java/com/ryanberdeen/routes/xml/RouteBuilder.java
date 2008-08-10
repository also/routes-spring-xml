package com.ryanberdeen.routes.xml;

import java.util.HashMap;
import java.util.HashSet;

import com.ryanberdeen.routes.UrlPattern;

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
	

	
	public String getPattern() {
		String patternPrefix = getMetaParameter("patternPrefix", "");
		String pattern = getMetaParameter("pattern");
		return patternPrefix + pattern;
	}

	public UrlPattern createUrlPattern() {
		return UrlPattern.parse(getPattern(), parameterValues.keySet(), parameterRegexes);
	}
	
	public HashSet<String> getMethods() {
		return getMethods(getMetaParameter("methods"));
	}
	
	public HashSet<String> getExcludedMethods() {
		return getMethods(getMetaParameter("excludedMethods"));
	}
	
	private static HashSet<String> getMethods(String value) {
		if (value == null || value.equals("any")) {
			return null;
		}
		String[] methodsArray = value.split(",");
		HashSet<String> methods = new HashSet<String>(methodsArray.length);
		for (String method : methodsArray) {
			methods.add(method.toUpperCase());
		}
		
		return methods;
	}
	
	public String getName() {
		String name = metaParameters.get("name");
		if (name != null) {
			name = getMetaParameter("namePrefix", "") + name;
		}
		
		return name;
	}
}
