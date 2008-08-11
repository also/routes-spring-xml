package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class WithBeanDefinitionParser extends AbstractRouteListParser {
	public void parseRouteList(ParserContext parserContext, Element element, ManagedList list, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.parseRouteParameters(element, routeBuilder);
		super.parseRouteList(parserContext, element, list, routeBuilder);
	}
}
