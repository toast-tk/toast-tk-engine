package io.toast.tk.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Base guice module to register new adapters in the engine	
 *
 */
public abstract class AbstractActionAdapterModule extends AbstractModule {

	protected final void bindActionAdapter(final Class<?> fixtureClass) {
		bind(fixtureClass).in(Singleton.class);
	}
}