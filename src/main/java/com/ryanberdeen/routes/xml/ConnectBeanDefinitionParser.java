package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.builder.RouteBuilder;

public class ConnectBeanDefinitionParser implements BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return parse(element, parserContext, null);
	}

	public BeanDefinition parse(Element element, ParserContext parserContext, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.parseRouteParameters(element, routeBuilder);

		return RouteParserUtils.createRouteBeanDefinition(element, parserContext, routeBuilder);
	}

}
