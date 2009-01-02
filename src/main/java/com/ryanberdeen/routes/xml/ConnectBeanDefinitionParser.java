package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.builder.RouteBuilder;
import com.ryanberdeen.routes.builder.RouteSetBuilder;

public class ConnectBeanDefinitionParser extends AbstractRouteListParser {

	public void parse(Element element, ParserContext parserContext, RouteSetBuilder routeSetBuilder) {
		RouteBuilder routeBuilder = routeSetBuilder.match();
		RouteParserUtils.parseRouteParameters(element, routeBuilder);
	}

}
