package com.synaptix.toast.runtime.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.block.BlockRunnerProvider;
import com.synaptix.toast.runtime.block.IBlockRunner;
import com.synaptix.toast.runtime.block.SwingPageBlockBuilder;
import com.synaptix.toast.runtime.block.TestBlockRunner;
import com.synaptix.toast.runtime.block.VariableBlockBuilder;
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

		MapBinder<Class, IBlockRunner> runnerMapBinder = MapBinder.newMapBinder(binder(), Class.class, IBlockRunner.class);
		runnerMapBinder.addBinding(WebPageBlock.class).to(WebPageBlockBuilder.class);
		runnerMapBinder.addBinding(TestBlock.class).to(TestBlockRunner.class);
		runnerMapBinder.addBinding(SwingPageBlock.class).to(SwingPageBlockBuilder.class);
		runnerMapBinder.addBinding(VariableBlock.class).to(VariableBlockBuilder.class);



	}


}