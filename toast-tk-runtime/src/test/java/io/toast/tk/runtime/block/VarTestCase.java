package io.toast.tk.runtime.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import io.toast.tk.core.annotation.EngineEventBus;
import io.toast.tk.core.driver.IRemoteSwingAgentDriver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.toast.tk.adapter.cache.ToastCache;
import io.toast.tk.dao.core.report.FailureResult;
import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;
import io.toast.tk.runtime.ActionItemRepository;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.action.item.ActionItemValueProvider;
import io.toast.tk.runtime.bean.ActionCommandDescriptor;
import io.toast.tk.runtime.block.locator.ActionAdaptaterLocator;
import io.toast.tk.runtime.block.locator.ActionAdaptaterLocators;
import io.toast.tk.runtime.block.locator.NoActionAdapterFound;
import io.toast.tk.runtime.module.RunnerModule;
import io.toast.tk.test.runtime.resource.XmlAdapterExample;

import io.toast.tk.test.runtime.mock.DummyRemoteEngineSwingDriver;

public class VarTestCase {

	private static Injector injector;
	TestBlockRunner blockRunner;
	IActionItemRepository repository;

	@BeforeClass
	public static void init() {
		ToastCache.getInstance().addActionAdapter(XmlAdapterExample.class);
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(EventBus.class).annotatedWith(EngineEventBus.class)
						.to(EventBus.class).in(Singleton.class);

				bind(IActionItemRepository.class).to(ActionItemRepository.class).in(Singleton.class);
				bind(ActionItemValueProvider.class).in(Singleton.class);
				bind(XmlAdapterExample.class).in(Singleton.class);
				
				//hack tps pour le rerun
				bind(IRemoteSwingAgentDriver.class).to(DummyRemoteEngineSwingDriver.class).in(Singleton.class);
			}
		};
		injector = Guice.createInjector(module, new RunnerModule());
	}

	@Before
	public void initRunner() {
		this.blockRunner = injector.getInstance(TestBlockRunner.class);
		this.repository = injector.getInstance(IActionItemRepository.class);
	}

	@Test
	public void executeStringTest() throws NoActionAdapterFound {
		TestLine line = new TestLine();
		line.setTest("echo *toto*");
		line.setExpected("$var");

		TestBlock block = new TestBlock();
		block.setFixtureName("service");
		block.setBlockLines(Collections.singletonList(line));

		ActionAdaptaterLocator locator = injector.getInstance(ActionAdaptaterLocators.class).getActionCommandDescriptor(block, line);
		ITestResult result = blockRunner.invokeActionAdapterAction(locator);
		String varValue = (String) this.repository.getUserVariables().get("$var");
		Assert.assertEquals("toto", varValue);	
		Assert.assertEquals(varValue, result.getMessage());
		Assert.assertEquals(ITestResult.ResultKind.SUCCESS, result.getResultKind());
	}

}
