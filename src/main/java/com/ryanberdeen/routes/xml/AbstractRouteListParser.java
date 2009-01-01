package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.builder.RouteBuilder;

public abstract class AbstractRouteListParser extends AbstractSingleBeanDefinitionParser implements RouteListParser {
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ListFactoryBean.class;
	}
	
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		ManagedList list = new ManagedList();
		RouteBuilder routeBuilder = new RouteBuilder();
		parseRouteList(new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), builder.getRawBeanDefinition()), element, list, routeBuilder);
		applyRouteList(parserContext, element, list, builder);
	}
	
	protected void applyRouteList(ParserContext parserContext, Element element, ManagedList list, BeanDefinitionBuilder builder) {
		builder.addPropertyValue("sourceList", list);
	}
	
	@SuppressWarnings("unchecked")
	public void parseRouteList(ParserContext parserContext, Element element, ManagedList list, RouteBuilder routeBuilder) {
		NodeList children = element.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				
				String name = child.getTagName();
				if (name.equals("resource")) {
					new ResourceBeanDefinitionParser().parseRouteList(parserContext, child, list, routeBuilder);
				}
				else if (name.equals("with")) {
					new WithBeanDefinitionParser().parseRouteList(parserContext, child, list, routeBuilder);
				}
				else if (name.equals("connect")) {
					list.add(new ConnectBeanDefinitionParser().parse(child, parserContext, routeBuilder));
				}
			}
		}
	}

}
