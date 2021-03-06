package io.toast.tk.adapter.cache;

import java.util.HashMap;

import io.toast.tk.adapter.FixtureDescriptor;

public class NoExceptionMap extends HashMap<Class<?>, FixtureDescriptor> {

	private static final long serialVersionUID = -5220092818033923858L;
	
	public NoExceptionMap(final int size) {
		super(size);
	}
	
	@Override
	public FixtureDescriptor putIfAbsent(
		final Class<?> fixtureDescriptorClass, 
		final FixtureDescriptor fixtureDescriptor
	) {
		if(fixtureDescriptorClass == Exception.class) {
			return null;
		}
		return super.putIfAbsent(fixtureDescriptorClass, fixtureDescriptor);
	}
}