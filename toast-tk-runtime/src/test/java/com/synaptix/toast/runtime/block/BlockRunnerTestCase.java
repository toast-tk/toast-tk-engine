package com.synaptix.toast.runtime.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.synaptix.toast.core.annotation.EngineEventBus;
import org.apache.commons.io.IOUtils;
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
import com.synaptix.toast.runtime.ActionItemRepository;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.test.runtime.resource.HttpAdapterExample;
import com.synaptix.toast.test.runtime.resource.JsonAdapterExample;
import com.synaptix.toast.test.runtime.resource.XmlAdapterExample;

public class BlockRunnerTestCase {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerTestCase.class);
	static String scenario;
	static Injector injector;

	@BeforeClass
	public static void init() {
		InputStream stream = BlockRunnerTestCase.class.getClassLoader().getResourceAsStream("./flux.scenario.txt");
		ToastCache.getInstance().addActionAdapter(XmlAdapterExample.class);
		ToastCache.getInstance().addActionAdapter(JsonAdapterExample.class);
		ToastCache.getInstance().addActionAdapter(HttpAdapterExample.class);
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(EventBus.class).annotatedWith(EngineEventBus.class).to(EventBus.class).in(Singleton.class);
				bind(IActionItemRepository.class).to(ActionItemRepository.class).in(Singleton.class);
				bind(ActionItemValueProvider.class).in(Singleton.class);
			}
		};
		injector = Guice.createInjector(module);
		try {
			scenario = IOUtils.toString(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParserMethodFinder() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		ActionCommandDescriptor method = blockRunner.findMatchingAction("Intégrer *$flux*", XmlAdapterExample.class);
		Assert.assertNotNull(method);
	}

	@Test
	public void testReverseEngineeringMethodParamType() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);

		Map<String, Object> userVarMap = new HashMap<>();
		String fluxValue = "<projet>" + "\n" +
				"<status>1</status>" + "\n" +
				"<name>projet</name>" + "\n" +
				"</projet>" + "\n";
		userVarMap.put("$flux", fluxValue);
		repo.setUserVariables(userVarMap);

		blockRunner.setInjector(injector);
		blockRunner.setObjectRepository(repo);

		ActionCommandDescriptor method = blockRunner.findMatchingAction("Integrate *$flux*", XmlAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 1);
		Assert.assertNotNull(args[0]);
	}

	@Test
	public void testRunnerArgumentBuilderXml() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);

		Map<String, Object> userVarMap = new HashMap<>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("<projet>").append("\n");
		fluxValue.append("<status>1</status>").append("\n");
		fluxValue.append("<name>projet</name>").append("\n");
		fluxValue.append("</projet>").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);

		blockRunner.setInjector(injector);
		blockRunner.setObjectRepository(repo);

		ActionCommandDescriptor method = blockRunner.findMatchingAction("Intégrer *$flux*", XmlAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 1);
		Assert.assertNotNull(args[0]);
	}

	@Test
	public void testRunnerArgumentBuilderJson() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);

		Map<String, Object> userVarMap = new HashMap<>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("{").append("\n");
		fluxValue.append("\"status\":\"1\",").append("\n");
		fluxValue.append("\"id\":\"1\",").append("\n");
		fluxValue.append("\"name\":\"projet\"}").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);

		blockRunner.setInjector(injector);
		blockRunner.setObjectRepository(repo);

		ActionCommandDescriptor method = blockRunner.findMatchingAction("Intégrer *$flux*", JsonAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 1);
		Assert.assertNotNull(args[0]);
	}

	@Test
	public void testRunnerArgumentBuilderJsonAndVar() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);
		
		Map<String, Object> userVarMap = new HashMap<>();
		StringBuilder userValue = new StringBuilder();
		userValue.append("{").append("\n");
		userValue.append("\"password\":\"mdp\",").append("\n");
		userValue.append("\"name\":\"user\"}").append("\n");
		userVarMap.put("$json", userValue.toString());
		userVarMap.put("$url", "https://www.google.com");
		repo.setUserVariables(userVarMap);

		blockRunner.setInjector(injector);
		blockRunner.setObjectRepository(repo);

		ActionCommandDescriptor method = blockRunner.findMatchingAction("POST *$json* to *$url*", HttpAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 2);
		Assert.assertEquals(args[1], repo.getUserVariables().get("$url"));
	}

}
