package com.ryanberdeen.routes.xml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.UrlPattern;
import com.ryanberdeen.routes.builder.RouteBuilder;

public class RouteParserUtils {

	private static final String URL_PATTERN = "urlPattern";
	private static final String EXCLUDED_METHODS = "excludedMethods";
	private static final String METHODS = "methods";
	private static final String PATTERN = "pattern";
	private static final String DEFAULT = "default";
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private static final String PARAMETER = "parameter";

	/** Parses the element, using only parameters from the parameterValues argument.
	 */
	public static BeanDefinition createRouteBeanDefinition(Element element, ParserContext parserContext, RouteBuilder routeBuilder) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Route.class);
		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));

		builder.addConstructorArgValue(routeBuilder.getPattern());
		builder.addConstructorArgValue(routeBuilder.parameterValues);
		builder.addConstructorArgValue(routeBuilder.parameterRegexes);
		addPropertyValues(builder, routeBuilder);

		return builder.getBeanDefinition();
	}

	public static BeanDefinition createAppliedRouteBeanDefinition(Element element, ParserContext parserContext, UrlPattern pattern, RouteBuilder routeBuilder, Map<String, String> applyParameters) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Route.class);

		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));

		HashMap<String, String> routeParameters = (HashMap<String, String>) routeBuilder.parameterValues.clone();
		routeParameters.putAll(applyParameters);

		UrlPattern appliedPattern = pattern.apply(applyParameters, routeBuilder.parameterValues);

		builder.addPropertyValue(URL_PATTERN, appliedPattern);
		builder.addPropertyValue("staticParameters", routeParameters);
		addPropertyValues(builder, routeBuilder);

		return builder.getBeanDefinition();
	}

	public static BeanDefinition createRouteBeanDefinition(Element element, ParserContext parserContext, UrlPattern pattern, RouteBuilder routeBuilder) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Route.class);

		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));

		builder.addPropertyValue(URL_PATTERN, pattern);
		builder.addPropertyValue("staticParameters", routeBuilder.parameterValues);
		addPropertyValues(builder, routeBuilder);

		return builder.getBeanDefinition();
	}

	private static void addPropertyValues(BeanDefinitionBuilder builder, RouteBuilder routeBuilder) {
		builder.addPropertyValue("defaultStaticParameters", routeBuilder.defaultStaticParameterValues);
		builder.addPropertyValue(NAME, routeBuilder.getName());
		builder.addPropertyValue(METHODS, routeBuilder.getMethods());
		builder.addPropertyValue(EXCLUDED_METHODS, routeBuilder.getExcludedMethods());
	}

	public static RouteBuilder parseRouteParameters(Element element, RouteBuilder routeBuilder) {
		RouteBuilder result = new RouteBuilder(routeBuilder);

		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); ++i) {
			Node node = attributes.item(i);
			String value = node.getTextContent();
			String parameterName = node.getLocalName();
			if (node.getNamespaceURI() != null) {
				result.setOption(parameterName, value);
			}
			else {
				result.parameterValues.put(parameterName, value);
			}
		}

		parseRouteParameterTags(element, result);

		return result;
	}

	public static void parseRouteParameterTags(Element element, RouteBuilder routeBuilder) {
		NodeList children = element.getElementsByTagName(PARAMETER);

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				parseParameterTag(child, routeBuilder);
			}
		}
	}

	private static void parseParameterTag(Element parameterTag, RouteBuilder routeBuilder) {
		String name = parameterTag.getAttribute(NAME);
		if (parameterTag.hasAttribute(VALUE)) {
			String value = parameterTag.getAttribute(VALUE);
			routeBuilder.parameterValues.put(name, value);
			if (parameterTag.hasAttribute(DEFAULT)) {
				boolean defaultStaticParameter = Boolean.valueOf(parameterTag.getAttribute(DEFAULT));
				if (defaultStaticParameter) {
					routeBuilder.defaultStaticParameterValues.put(name, value);
				}
			}
		}
		if (parameterTag.hasAttribute(PATTERN)) {
			routeBuilder.parameterRegexes.put(name, parameterTag.getAttribute(PATTERN));
		}
	}
}
