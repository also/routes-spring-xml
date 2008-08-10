package com.ryanberdeen.routes.xml;

import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.UrlPattern;

public class RouteParserUtils {
	
	public static Route createRoute(RouteBuilder routeBuilder) {
		Route result = new Route(getPattern(routeBuilder), routeBuilder.parameterValues, routeBuilder.parameterRegexes);
		result.setDefaultStaticParameters(routeBuilder.defaultStaticParameterValues);
		result.setName(getName(routeBuilder));
		result.setMethods(getMethods(routeBuilder));
		result.setExcludedMethods(getExcludedMethods(routeBuilder));
		
		return result;
	}
	
	public static UrlPattern createUrlPattern(RouteBuilder routeBuilder) {
		return UrlPattern.parse(getPattern(routeBuilder), routeBuilder.parameterValues.keySet(), routeBuilder.parameterRegexes);
	}
	
	/** Parses the element, using only parameters from the parameterValues argument.
	 */
	public static BeanDefinition createRouteBeanDefinition(Element element, ParserContext parserContext, RouteBuilder routeBuilder) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Route.class);
		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));

		builder.addConstructorArgValue(getPattern(routeBuilder));
		builder.addConstructorArgValue(routeBuilder.parameterValues);
		builder.addConstructorArgValue(routeBuilder.parameterRegexes);
		addPropertyValues(builder, routeBuilder);
		
		return builder.getBeanDefinition();
	}
	
	public static BeanDefinition createAppliedRouteBeanDefinition(Element element, ParserContext parserContext, UrlPattern pattern, RouteBuilder baseParameters, RouteBuilder applyParameters) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Route.class);

		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
		
		HashMap<String, String> routeParameters = (HashMap<String, String>) baseParameters.parameterValues.clone();
		routeParameters.putAll(applyParameters.parameterValues);
		
		UrlPattern appliedPattern = pattern.apply(applyParameters.parameterValues, baseParameters.parameterValues);
		
		builder.addPropertyValue("urlPattern", appliedPattern);
		builder.addPropertyValue("staticParameters", routeParameters);
		addPropertyValues(builder, baseParameters);
		
		return builder.getBeanDefinition();
	}
	
	public static BeanDefinition createRouteBeanDefinition(Element element, ParserContext parserContext, UrlPattern pattern, RouteBuilder routeBuilder) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Route.class);

		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));

		builder.addPropertyValue("urlPattern", pattern);
		builder.addPropertyValue("staticParameters", routeBuilder.parameterValues);
		addPropertyValues(builder, routeBuilder);
		
		return builder.getBeanDefinition();
	}
	
	private static void addPropertyValues(BeanDefinitionBuilder builder, RouteBuilder routeBuilder) {
		builder.addPropertyValue("defaultStaticParameters", routeBuilder.defaultStaticParameterValues);
		builder.addPropertyValue("name", getName(routeBuilder));
		builder.addPropertyValue("methods", getMethods(routeBuilder));
		builder.addPropertyValue("excludedMethods", getExcludedMethods(routeBuilder));
	}
	
	public static HashSet<String> getMethods(RouteBuilder routeBuilder) {
		return getMethods(routeBuilder.getMetaParameter("methods"));
	}
	
	public static HashSet<String> getExcludedMethods(RouteBuilder routeBuilder) {
		return getMethods(routeBuilder.getMetaParameter("excludedMethods"));
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
	
	private static String getName(RouteBuilder routeBuilder) {
		String name = routeBuilder.metaParameters.get("name");
		if (name != null) {
			name = routeBuilder.getMetaParameter("namePrefix", "") + name;
		}
		
		return name;
	}
	
	private static String getPattern(RouteBuilder routeBuilder) {
		String patternPrefix = routeBuilder.getMetaParameter("patternPrefix", "");
		String pattern = routeBuilder.getMetaParameter("pattern");
		return patternPrefix + pattern;
	}
	
	public static RouteBuilder applyRouteParameters(Element element, RouteBuilder routeBuilder) {
		RouteBuilder result = new RouteBuilder(routeBuilder);
		
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); ++i) {
			Node node = attributes.item(i);
			String value = node.getTextContent();
			String parameterName = node.getLocalName();
			if (node.getNamespaceURI() != null) {
				result.metaParameters.put(parameterName, value);
			}
			else {
				result.parameterValues.put(parameterName, value);
			}
		}
		
		applyRouteParameterTags(element, result);
		
		return result;
	}
	
	public static void applyRouteParameterTags(Element element, RouteBuilder routeBuilder) {
		NodeList children = element.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				if (child.getTagName().equals("parameter")) {
					applyParameter(child, routeBuilder);
				}
			}
		}
	}

	private static void applyParameter(Element element, RouteBuilder routeBuilder) {
		String name = element.getAttribute("name");
		if (element.hasAttribute("value")) {
			String value = element.getAttribute("value");
			routeBuilder.parameterValues.put(name, value);
			if (element.hasAttribute("default")) {
				boolean defaultStaticParameter = Boolean.valueOf(element.getAttribute("default"));
				if (defaultStaticParameter) {
					routeBuilder.defaultStaticParameterValues.put(name, value);
				}
			}
		}
		if (element.hasAttribute("pattern")) {
			routeBuilder.parameterRegexes.put(name, element.getAttribute("pattern"));
		}
	}
}
