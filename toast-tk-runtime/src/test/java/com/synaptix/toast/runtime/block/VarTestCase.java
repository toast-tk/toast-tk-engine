package com.synaptix.toast.runtime.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.synaptix.toast.core.annotation.EngineEventBus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.synaptix.toast.adapter.cache.ToastCache;
import com.synaptix.toast.core.report.FailureResult;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.ActionItemRepository;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.block.locator.ActionAdaptaterLocator;
import com.synaptix.toast.runtime.block.locator.ActionAdaptaterLocators;
import com.synaptix.toast.runtime.block.locator.NoActionAdapterFound;
import com.synaptix.toast.runtime.module.RunnerModule;
import com.synaptix.toast.test.runtime.resource.XmlAdapterExample;

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
