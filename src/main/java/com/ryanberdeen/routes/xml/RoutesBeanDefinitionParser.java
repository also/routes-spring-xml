package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.ryanberdeen.routes.builder.RouteBuilder;

public class RoutesBeanDefinitionParser extends AbstractRouteListParser {
	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
		if (element.hasAttribute(ID_ATTRIBUTE)) {
			return element.getAttribute(ID_ATTRIBUTE);
		}
		else {
			return parserContext.getReaderContext().generateBeanName(definition);
		}
	}

	@Override
	public void parseRouteList(ParserContext parserContext, Element element, ManagedList list, RouteBuilder routeBuilder) {
		RouteParserUtils.parseRouteParameterTags(element, routeBuilder);
		super.parseRouteList(parserContext, element, list, routeBuilder);
	}
}
