package com.ryanberdeen.routes.xml;

import java.util.List;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.builder.RouteBuilder;

public class WithBeanDefinitionParser extends AbstractRouteListParser {
	public void parseRouteList(ParserContext parserContext, Element element, List<Route> routes, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.parseRouteParameters(element, routeBuilder);
		super.parseRouteList(parserContext, element, routes, routeBuilder);
	}
}
