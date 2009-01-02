package com.ryanberdeen.routes.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.builder.RouteOptions;

public class RouteParserUtils {
	private static final String PATTERN = "pattern";
	private static final String DEFAULT = "default";
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private static final String PARAMETER = "parameter";

	public static void parseRouteParameters(Element element, RouteOptions routeOptions) {
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); ++i) {
			Node node = attributes.item(i);
			String value = node.getTextContent();
			String parameterName = node.getLocalName();
			if (node.getNamespaceURI() != null) {
				routeOptions.setOption(parameterName, value);
			}
			else {
				routeOptions.setParameterValue(parameterName, value);
			}
		}

		parseRouteParameterTags(element, routeOptions);
	}

	public static void parseRouteParameterTags(Element element, RouteOptions routeOptions) {
		NodeList children = element.getElementsByTagName(PARAMETER);

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				parseParameterTag(child, routeOptions);
			}
		}
	}

	private static void parseParameterTag(Element parameterTag, RouteOptions routeOptions) {
		String name = parameterTag.getAttribute(NAME);
		if (parameterTag.hasAttribute(VALUE)) {
			String value = parameterTag.getAttribute(VALUE);
			routeOptions.setParameterValue(name, value);
			if (parameterTag.hasAttribute(DEFAULT)) {
				boolean defaultStaticParameter = Boolean.valueOf(parameterTag.getAttribute(DEFAULT));
				if (defaultStaticParameter) {
					routeOptions.setDefaultStaticParameterValue(name, value);
				}
			}
		}
		if (parameterTag.hasAttribute(PATTERN)) {
			routeOptions.setParameterRegex(name, parameterTag.getAttribute(PATTERN));
		}
	}
}
