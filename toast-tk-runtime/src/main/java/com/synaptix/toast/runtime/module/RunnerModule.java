package com.synaptix.toast.runtime.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.block.BlockRunnerProvider;
import com.synaptix.toast.runtime.block.SwingPageBlockBuilder;
import com.synaptix.toast.runtime.block.TestBlockRunner;
import com.synaptix.toast.runtime.block.WebPageBlockBuilder;
import com.synaptix.toast.runtime.block.locator.FixtureServicesLocator;

public class RunnerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BlockRunnerProvider.class).in(Singleton.class);
		bind(ActionItemValueProvider.class).in(Singleton.class);
		bind(SwingPageBlockBuilder.class).in(Singleton.class);
		bind(WebPageBlockBuilder.class).in(Singleton.class);
		bind(TestBlockRunner.class).in(Singleton.class);
		bind(FixtureServicesLocator.class).in(Singleton.class);
	}
}