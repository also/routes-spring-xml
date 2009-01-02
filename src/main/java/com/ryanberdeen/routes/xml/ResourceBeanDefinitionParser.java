package com.ryanberdeen.routes.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.UrlPattern;
import com.ryanberdeen.routes.builder.RouteBuilder;

public class ResourceBeanDefinitionParser extends AbstractRouteListParser {

	public void parseRouteList(ParserContext parserContext, Element element, List<Route> routes, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.parseRouteParameters(element, routeBuilder);

		NodeList children = element.getChildNodes();

		UrlPattern prefix = routeBuilder.createUrlPattern();

		UrlPattern collectionActionPattern = null;
		UrlPattern memberActionPattern = null;

		ArrayList<Route> collectionActions = null;
		ArrayList<Route> memberActions = null;

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;

				if (child.getTagName().equals("collection")) {
					collectionActionPattern = prefix.append(routeBuilder.getCollectionPattern());
					collectionActions = parseApplyTags(parserContext, child, collectionActionPattern, routeBuilder);
				}
				else if (child.getTagName().equals("member")) {
					memberActionPattern = prefix.append(routeBuilder.getMemberPattern());
					memberActions = parseApplyTags(parserContext, child, memberActionPattern, routeBuilder);
				}
			}
		}

		if (collectionActionPattern == null) {
			collectionActionPattern = prefix.append(routeBuilder.getCollectionPattern());
		}

		Map<String, String> appliedParameters;

		RouteBuilder collectionBuilder = routeBuilder.createCollectionBuilder();
		for (String action : collectionBuilder.defaultResourceCollectionActions) {
			appliedParameters = Collections.singletonMap(collectionBuilder.getActionParamterName(), action);
			routes.add(RouteParserUtils.createAppliedRoute(element, parserContext, collectionActionPattern, routeBuilder, appliedParameters));
		}

		if (collectionActions != null) {
			routes.addAll(collectionActions);
		}

		if (memberActionPattern == null) {
			memberActionPattern = prefix.append(routeBuilder.getMemberPattern());
		}

		RouteBuilder memberBuilder = routeBuilder.createMemberBuilder();

		for (String action : memberBuilder.defaultResourceMemberActions) {
			appliedParameters = Collections.singletonMap(collectionBuilder.getActionParamterName(), action);
			routes.add(RouteParserUtils.createAppliedRoute(element, parserContext, memberActionPattern, memberBuilder, appliedParameters));
		}

		if (memberActions != null) {
			routes.addAll(memberActions);
		}
	}

	private static ArrayList<Route> parseApplyTags(ParserContext parserContext, Element element, UrlPattern pattern, RouteBuilder baseParameters) {
		ArrayList<Route> result = new ArrayList<Route>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				RouteBuilder applyParameters = new RouteBuilder();
				RouteBuilder appliedParameters = RouteParserUtils.parseRouteParameters(child, applyParameters);

				result.add(RouteParserUtils.createAppliedRoute(element, parserContext, pattern, baseParameters, appliedParameters.parameterValues));
			}
		}

		return result;
	}
}
