package io.toast.tk.runtime.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;

import io.toast.tk.dao.domain.impl.test.block.SwingPageBlock;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.VariableBlock;
import io.toast.tk.dao.domain.impl.test.block.WebPageBlock;
import io.toast.tk.runtime.action.item.ActionItemValueProvider;
import io.toast.tk.runtime.block.BlockRunnerProvider;
import io.toast.tk.runtime.block.IBlockRunner;
import io.toast.tk.runtime.block.SwingPageBlockBuilder;
import io.toast.tk.runtime.block.TestBlockRunner;
import io.toast.tk.runtime.block.VariableBlockBuilder;
import io.toast.tk.runtime.block.WebPageBlockBuilder;
import io.toast.tk.runtime.block.locator.FixtureServicesLocator;

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