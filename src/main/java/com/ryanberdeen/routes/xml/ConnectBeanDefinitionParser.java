package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.builder.RouteBuilder;

public class ConnectBeanDefinitionParser extends AbstractRouteListParser {

	public Route parse(Element element, ParserContext parserContext, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.parseRouteParameters(element, routeBuilder);

		return RouteParserUtils.createRoute(element, parserContext, routeBuilder);
	}

}
