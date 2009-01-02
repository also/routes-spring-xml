package com.ryanberdeen.routes.spring;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import com.ryanberdeen.routes.Route;
import com.ryanberdeen.routes.RouteSet;

public class RouteSetFactory implements FactoryBean {
	private List<Route> routes;
	
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	public Object getObject() throws Exception {
		RouteSet routeSet = new RouteSet();
		routeSet.setRoutes(routes);
		return routeSet;
	}

	public Class<RouteSet> getObjectType() {
		return RouteSet.class;
	}

	public boolean isSingleton() {
		return false;
	}
}
