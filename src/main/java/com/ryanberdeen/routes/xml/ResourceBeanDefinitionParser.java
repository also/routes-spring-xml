package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.builder.RouteBuilder;
import com.ryanberdeen.routes.builder.RouteSetBuilder;

public class ResourceBeanDefinitionParser extends AbstractRouteListParser {
	public void parseRouteList(ParserContext parserContext, Element element, RouteSetBuilder routeSetBuilder) {
		routeSetBuilder = routeSetBuilder.template("resource");
		RouteParserUtils.parseRouteParameters(element, routeSetBuilder);

		NodeList children = element.getChildNodes();

		RouteSetBuilder collectionRouteSetBuilder = routeSetBuilder.template("collection");
		RouteSetBuilder memberRouteSetBuilder = routeSetBuilder.template("member");

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;

				if (child.getTagName().equals("collection")) {
					parseApplyTags(parserContext, child, collectionRouteSetBuilder);
				}
				else if (child.getTagName().equals("member")) {
					parseApplyTags(parserContext, child, memberRouteSetBuilder);
				}
			}
		}
	}

	private static void parseApplyTags(ParserContext parserContext, Element element, RouteSetBuilder routeSetBuilder) {
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				// TODO don't use route builder
				Element child =  (Element) node;
				RouteBuilder appliedParameters = new RouteBuilder();
				RouteParserUtils.parseRouteParameters(child, appliedParameters);

				routeSetBuilder.apply(appliedParameters);
			}
		}
	}
}
