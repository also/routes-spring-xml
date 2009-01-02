package com.ryanberdeen.routes.spring;

import org.springframework.beans.factory.FactoryBean;

import com.ryanberdeen.routes.RouteSet;
import com.ryanberdeen.routes.builder.RouteSetBuilder;

public class RouteSetFactory implements FactoryBean {
	private RouteSetBuilder routeSetBuilder;
	
	public void setRouteSetBuilder(RouteSetBuilder routeSetBuilder) {
		this.routeSetBuilder = routeSetBuilder;
	}
	
	public Object getObject() throws Exception {
		return routeSetBuilder.createRouteSet();
	}

	public Class<RouteSet> getObjectType() {
		return RouteSet.class;
	}

	public boolean isSingleton() {
		return false;
	}
}
