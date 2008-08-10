package com.ryanberdeen.routes.xml;

import java.util.ArrayList;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ryanberdeen.routes.UrlPattern;

public class ResourceBeanDefinitionParser extends AbstractRouteListParser {
	private static final String GET_METHOD = "GET";
	private static final String POST_METHOD = "POST";
	private static final String PUT_METHOD = "PUT";
	private static final String DELETE_METHOD = "DELETE";

	private static final String INDEX_ACTION = "index";
	private static final String SHOW_ACTION = "show";
	private static final String CREATE_ACTION = "create";
	private static final String UPDATE_ACTION = "update";
	private static final String DESTROY_ACTION = "destroy";
	
	private static RouteBuilder applyParameters(RouteBuilder baseParameters, String method, String parameterName, String action) {
		RouteBuilder result = new RouteBuilder();
		result.parameterValues.put(parameterName, action);
		baseParameters.metaParameters.put("methods", method);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void parseRouteList(ParserContext parserContext, Element element, ManagedList list, RouteBuilder routeBuilder) {
		routeBuilder = RouteParserUtils.applyRouteParameters(element, routeBuilder);
		
		String actionParamterName = "action";
		
		NodeList children = element.getChildNodes();
		
		UrlPattern prefix = RouteParserUtils.createUrlPattern(routeBuilder);

		String idParameterName = routeBuilder.getMetaParameter("idParameter", "id");
		
		UrlPattern collectionPattern = null;
		UrlPattern memberPattern = null;
		
		ArrayList<BeanDefinition> collectionActions = null;
		ArrayList<BeanDefinition> memberActions = null;
		
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				
				if (child.getTagName().equals("collection")) {
					collectionPattern = prefix.append(routeBuilder.getMetaParameter("collectionPattern", ":action"));
					collectionActions = parseApplys(parserContext, child, collectionPattern, routeBuilder);
				}
				else if (child.getTagName().equals("member")) {
					memberPattern = prefix.append(':' + idParameterName + "/:" + actionParamterName);
					memberActions = parseApplys(parserContext, child, memberPattern, routeBuilder);
				}
			}
		}
		
		if (collectionPattern == null) {
			collectionPattern = prefix.append(routeBuilder.getMetaParameter("collectionPattern", ":" + actionParamterName));
		}
		
		RouteBuilder appliedParameters;

		RouteBuilder collectionParameters = new RouteBuilder(routeBuilder);
		collectionParameters.parameterValues.put(actionParamterName, INDEX_ACTION);
		appliedParameters = applyParameters(routeBuilder, GET_METHOD, actionParamterName, INDEX_ACTION);
		list.add(RouteParserUtils.createAppliedRouteBeanDefinition(element, parserContext, collectionPattern, collectionParameters, appliedParameters));
		
		if (collectionActions != null) {
			list.addAll(collectionActions);
		}
		
		if (memberPattern == null) {
			memberPattern = prefix.append(':' + idParameterName + "/:" + actionParamterName);
		}
		
		RouteBuilder memberParameters = new RouteBuilder(routeBuilder);
		memberParameters.parameterValues.put(actionParamterName, SHOW_ACTION);
		memberParameters.defaultStaticParameterValues.put(actionParamterName, SHOW_ACTION);
		
		appliedParameters = applyParameters(routeBuilder, GET_METHOD, actionParamterName, SHOW_ACTION);
		list.add(RouteParserUtils.createAppliedRouteBeanDefinition(element, parserContext, memberPattern, memberParameters, appliedParameters));
		
		appliedParameters = applyParameters(routeBuilder, POST_METHOD, actionParamterName, CREATE_ACTION);
		list.add(RouteParserUtils.createAppliedRouteBeanDefinition(element, parserContext, memberPattern, memberParameters, appliedParameters));
		
		appliedParameters = applyParameters(routeBuilder, PUT_METHOD, actionParamterName, UPDATE_ACTION);
		list.add(RouteParserUtils.createAppliedRouteBeanDefinition(element, parserContext, memberPattern, memberParameters, appliedParameters));
		
		appliedParameters = applyParameters(routeBuilder, DELETE_METHOD, actionParamterName, DESTROY_ACTION);
		list.add(RouteParserUtils.createAppliedRouteBeanDefinition(element, parserContext, memberPattern, memberParameters, appliedParameters));
		
		if (memberActions != null) {
			list.addAll(memberActions);
		}
	}

	private static ArrayList<BeanDefinition> parseApplys(ParserContext parserContext, Element element, UrlPattern pattern, RouteBuilder baseParameters) {
		ArrayList<BeanDefinition> result = new ArrayList<BeanDefinition>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child =  (Element) node;
				RouteBuilder applyParameters = new RouteBuilder();
				RouteBuilder appliedParameters = RouteParserUtils.applyRouteParameters(child, applyParameters);
				
				result.add(RouteParserUtils.createAppliedRouteBeanDefinition(element, parserContext, pattern, baseParameters, appliedParameters));
			}
		}
		
		return result;
	}
}
