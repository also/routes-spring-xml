package com.ryanberdeen.routes.xml;

import java.util.List;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.builder.RouteBuilder;

public interface RouteListParser extends BeanDefinitionParser {
	public void parseRouteList(ParserContext parserContext, Element element, List<Route> routes, RouteBuilder routeBuilder);
}
