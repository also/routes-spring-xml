package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.builder.RouteBuilder;
import com.ryanberdeen.routes.builder.RouteSetBuilder;
import com.ryanberdeen.routes.spring.RouteSetFactory;

public abstract class AbstractRouteListParser extends AbstractSingleBeanDefinitionParser implements RouteListParser {
	@Override
	protected Class<?> getBeanClass(Element element) {
		return RouteSetFactory.class;
	}
	
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		RouteSetBuilder routeSetBuilder = new RouteSetBuilder();
		RouteBuilder routeBuilder = new RouteBuilder();
		parseRouteList(new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), builder.getRawBeanDefinition()), element, routeSetBuilder, routeBuilder);
		builder.addPropertyValue("routeSetBuilder", routeSetBuilder);
	}
	
	public void parseRouteList(ParserContext parserContext, Element element, RouteSetBuilder routeSetBuilder, RouteBuilder routeBuilder) {
		NodeList children = element.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				
				String name = child.getTagName();
				if (name.equals("resource")) {
					new ResourceBeanDefinitionParser().parseRouteList(parserContext, child, routeSetBuilder, routeBuilder);
				}
				else if (name.equals("with")) {
					new WithBeanDefinitionParser().parseRouteList(parserContext, child, routeSetBuilder, routeBuilder);
				}
				else if (name.equals("connect")) {
					routeSetBuilder.add(new ConnectBeanDefinitionParser().parse(child, parserContext, routeBuilder));
				}
			}
		}
	}
}
