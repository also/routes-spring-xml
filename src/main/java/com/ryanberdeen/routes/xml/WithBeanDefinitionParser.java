package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.builder.RouteBuilder;
import com.ryanberdeen.routes.builder.RouteSetBuilder;

public class WithBeanDefinitionParser extends AbstractRouteListParser {
	public void parseRouteList(ParserContext parserContext, Element element, RouteSetBuilder routeSetBuilder, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.parseRouteParameters(element, routeBuilder);
		super.parseRouteList(parserContext, element, routeSetBuilder, routeBuilder);
	}
}
