package com.synaptix.toast.core.guice;

import java.lang.annotation.Annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

public abstract class AbstractComponentAdapterModule extends AbstractModule {

	Multibinder<ICustomRequestHandler> uriCustomFixtureHandlerBinder;

	Multibinder<FilteredAWTEventListener> uriAwtEventListenerBinder;

	@Override
	protected void configure() {
		this.uriCustomFixtureHandlerBinder = Multibinder.newSetBinder(binder(), ICustomRequestHandler.class);
		this.uriAwtEventListenerBinder = Multibinder.newSetBinder(binder(), FilteredAWTEventListener.class);
		configureModule();
	}

	protected abstract void configureModule();

	protected final void addCustomFilteredAWTEventListener(
		Class<? extends FilteredAWTEventListener> customFilteredAWTEventListenerClass) {
		bind(customFilteredAWTEventListenerClass).in(Singleton.class);
		uriAwtEventListenerBinder.addBinding().to(customFilteredAWTEventListenerClass);
	}

	protected final void addTypeHandler(
		Class<? extends ICustomRequestHandler> typeHandlerClass) {
		bind(typeHandlerClass).in(Singleton.class);
		uriCustomFixtureHandlerBinder.addBinding().to(typeHandlerClass);
	}

	protected final void addTypeHandler(
		final Class<? extends ICustomRequestHandler> typeHandlerClass,
		final Class<? extends Annotation> annotationClass
		) {
		bind(typeHandlerClass).annotatedWith(annotationClass).in(Singleton.class);
		uriCustomFixtureHandlerBinder.addBinding().to(typeHandlerClass);
	}
}