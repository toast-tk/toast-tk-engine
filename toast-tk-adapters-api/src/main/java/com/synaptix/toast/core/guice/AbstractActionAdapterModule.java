package com.synaptix.toast.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public abstract class AbstractActionAdapterModule extends AbstractModule {

	protected final void bindActionAdapter(
		Class<?> fixtureClass) {
		bind(fixtureClass).in(Singleton.class);
	}
}
