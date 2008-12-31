package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public interface RouteListParser extends BeanDefinitionParser {
	public void parseRouteList(ParserContext parserContext, Element element, ManagedList list, RouteBuilder routeBuilder);
}
