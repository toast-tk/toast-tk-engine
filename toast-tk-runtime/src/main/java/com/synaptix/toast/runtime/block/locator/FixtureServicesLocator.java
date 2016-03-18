package com.synaptix.toast.runtime.block.locator;

import java.util.List;

import com.google.inject.Injector;
import com.synaptix.toast.adapter.ActionAdapterCollector;
import com.synaptix.toast.adapter.FixtureService;

public class FixtureServicesLocator {

	private final List<FixtureService> fixtureApiServices;
	
	public FixtureServicesLocator(final Injector injector) {
		this.fixtureApiServices = ActionAdapterCollector.listAvailableServicesByInjection(injector);
	}
	
	private static FixtureServicesLocator INSTANCE;
	
	public static FixtureServicesLocator getFixtureServicesLocator(final Injector injector) {
		if(INSTANCE == null) {
			INSTANCE = new FixtureServicesLocator(injector);
		}
		return INSTANCE;
	}
	
	public List<FixtureService> getFixtureApiServices() {
		return fixtureApiServices;
	}
}