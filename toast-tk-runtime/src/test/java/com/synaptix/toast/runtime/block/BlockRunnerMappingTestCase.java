package com.synaptix.toast.runtime.block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.synaptix.toast.adapter.cache.ToastCache;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.ActionItemRepository;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.block.locator.ActionAdaptaterLocator;
import com.synaptix.toast.runtime.block.locator.ActionAdaptaterLocators;
import com.synaptix.toast.test.runtime.resource.XmlAdapterExample;

public class BlockRunnerMappingTestCase {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerMappingTestCase.class);
	static String scenario;
	static Injector injector;

	@BeforeClass
	public static void init() {
		ToastCache.getInstance().addActionAdapter(XmlAdapterExample.class);
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IActionItemRepository.class).to(ActionItemRepository.class).in(Singleton.class);
				bind(ActionItemValueProvider.class).in(Singleton.class);
				bind(XmlAdapterExample.class).in(Singleton.class);
			}
		};
		injector = Guice.createInjector(module);
	}

	@Test
	public void compareTest() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		String actionSentence = "Comparer *$var2* a *$var1*";
		ActionCommandDescriptor actionDescriptor = blockRunner.findMatchingAction(actionSentence, XmlAdapterExample.class);
		Assert.assertNotNull(actionDescriptor);
	}
	
	@Test
	public void executeVoidTest() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		String actionSentence = "*$var2* == *$var1*";
		ActionCommandDescriptor actionDescriptor = blockRunner.findMatchingAction(actionSentence, XmlAdapterExample.class);
		blockRunner.setInjector(injector);
		
		TestBlock block = new TestBlock();
		block.setFixtureName("service");
		TestLine line = new TestLine();
		line.setTest(actionSentence);
		block.setBlockLines(Arrays.asList(line));
		
		ActionAdaptaterLocator locator = ActionAdaptaterLocators.getInstance().getActionCommandDescriptor(block, line, injector);
		blockRunner.invokeActionAdapterAction(locator);
		
		Assert.assertNotNull(actionDescriptor);
	}
	
	@Test
	public void compareAndSwapInputsTest() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);
		Map<String, Object> userVarMap = new HashMap<>();
		userVarMap.put("$var1", "value1");
		userVarMap.put("$var2", "value2");
		repo.setUserVariables(userVarMap);
		blockRunner.setInjector(injector);
		blockRunner.setObjectRepository(repo);
		
		String actionSentence = "Comparer *$var2* a *$var1*";
		ToastCache.getInstance().addActionAdapter(XmlAdapterExample.class);
		ActionCommandDescriptor execDescriptor = blockRunner.findMatchingAction(actionSentence, XmlAdapterExample.class);
		System.out.println("BlockRunnerMappingTestCase.compareAndSwapInputsTest() -> " + execDescriptor);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(execDescriptor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 2);
		Assert.assertEquals("value1", args[0]);
		Assert.assertEquals("value2", args[1]);
	}
	
	@Test
	public void compareAndSwapInputsTest2() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);
		Map<String, Object> userVarMap = new HashMap<>();
		userVarMap.put("$var1", "value1");
		userVarMap.put("$var2", "value2");
		repo.setUserVariables(userVarMap);
		blockRunner.setInjector(injector);
		blockRunner.setObjectRepository(repo);
		
		String actionSentence = "Comparer *$var1* a *$var2*";
		ActionCommandDescriptor method = blockRunner.findMatchingAction(actionSentence, XmlAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 2);
		Assert.assertEquals("value2", args[0]);
		Assert.assertEquals("value1", args[1]);
	}
	
	@Test
	public void testMethodReplacement() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		String actionSentence = "Inclure le flux xml dans la variable *$flux*";
		ActionCommandDescriptor actionDescriptor = blockRunner.findMatchingAction(actionSentence, XmlAdapterExample.class);
		Assert.assertNotNull(actionDescriptor);
	}
	
	@Test
	public void testMethodReplacementWithTwoParamaters() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		String actionSentence = "Inclure dans la variable *$flux* le flux *$xml*";
		ActionCommandDescriptor actionDescriptor = blockRunner.findMatchingAction(actionSentence, XmlAdapterExample.class);
		Assert.assertNotNull(actionDescriptor);
	}

}
