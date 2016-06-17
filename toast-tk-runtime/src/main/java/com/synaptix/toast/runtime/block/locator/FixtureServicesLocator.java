package com.synaptix.toast.runtime.block.locator;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.synaptix.toast.adapter.ActionAdapterCollector;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.runtime.block.TestBlockRunner;

public class FixtureServicesLocator {

	private List<FixtureService> fixtureApiServices;

	@Inject
	private Injector injector;

	@Inject
	private TestBlockRunner testBlockRunner;

	public FixtureServicesLocator() {
	}
	
	public List<FixtureService> getFixtureApiServices() {
		if (fixtureApiServices == null) {
			fixtureApiServices = ActionAdapterCollector.listAvailableServicesByInjection(injector);
		}
		return fixtureApiServices;
	}
}