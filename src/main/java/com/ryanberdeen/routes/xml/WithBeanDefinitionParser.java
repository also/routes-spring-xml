package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.builder.RouteSetBuilder;

public class WithBeanDefinitionParser extends AbstractRouteListParser {
	public void parseRouteList(ParserContext parserContext, Element element, RouteSetBuilder routeSetBuilder) {
		routeSetBuilder = routeSetBuilder.nested();
		RouteParserUtils.parseRouteParameters(element, routeSetBuilder);
		super.parseRouteList(parserContext, element, routeSetBuilder);
	}
}
