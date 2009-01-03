package com.ryanberdeen.routes.xml;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.UrlPattern;
import com.ryanberdeen.routes.builder.ResourceTemplate;
import com.ryanberdeen.routes.builder.RouteBuilder;
import com.ryanberdeen.routes.builder.RouteSetBuilder;

public class ResourceBeanDefinitionParser extends AbstractRouteListParser {
	public void parseRouteList(ParserContext parserContext, Element element, RouteSetBuilder routeSetBuilder) {
		routeSetBuilder = routeSetBuilder.template("resource");
		RouteParserUtils.parseRouteParameters(element, routeSetBuilder);

		NodeList children = element.getChildNodes();

		UrlPattern prefix = routeSetBuilder.createUrlPattern();

		RouteSetBuilder collectionRouteSetBuilder = routeSetBuilder.template("collection");
		UrlPattern collectionActionPattern = prefix.append(collectionRouteSetBuilder.getOption(ResourceTemplate.COLLECTION_PATTERN_OPTION));
		RouteSetBuilder memberRouteSetBuilder = routeSetBuilder.template("member");
		UrlPattern memberActionPattern = prefix.append(memberRouteSetBuilder.getOption(ResourceTemplate.MEMBER_PATTERN_OPTION));

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;

				if (child.getTagName().equals("collection")) {
					parseApplyTags(parserContext, child, collectionActionPattern, collectionRouteSetBuilder);
				}
				else if (child.getTagName().equals("member")) {
					parseApplyTags(parserContext, child, memberActionPattern, memberRouteSetBuilder);
				}
			}
		}
	}

	private static void parseApplyTags(ParserContext parserContext, Element element, UrlPattern pattern, RouteSetBuilder routeSetBuilder) {
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				RouteBuilder appliedParameters = new RouteBuilder();
				RouteParserUtils.parseRouteParameters(child, appliedParameters);

				routeSetBuilder.apply(pattern, appliedParameters.parameterValues);
			}
		}
	}
}
