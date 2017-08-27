package io.toast.tk.runtime.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import io.toast.tk.core.annotation.EngineEventBus;
import io.toast.tk.runtime.ActionItemRepository;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.action.item.ActionItemValueProvider;
import io.toast.tk.runtime.bean.ActionCommandDescriptor;
import io.toast.tk.runtime.block.TestBlockRunner;
import io.toast.tk.test.runtime.resource.HttpAdapterExample;
import io.toast.tk.test.runtime.resource.JsonAdapterExample;
import io.toast.tk.test.runtime.resource.XmlAdapterExample;

public class BlockRunnerTestCase {

	static String scenario;
	static Injector injector;
	TestBlockRunner blockRunner;
	
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
				bind(TestBlockRunner.class);
			}
		};
		injector = Guice.createInjector(module);
		try {
			scenario = IOUtils.toString(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void initRunner(){
		this.blockRunner = injector.getInstance(TestBlockRunner.class);
	}

	@Test
	public void testParserMethodFinder() {
		ActionCommandDescriptor method = blockRunner.findMatchingAction("Intégrer *$flux*", XmlAdapterExample.class);
		Assert.assertNotNull(method);
	}
	

	@Test
	public void testReverseEngineeringMethodParamType() {
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);

		Map<String, Object> userVarMap = new HashMap<>();
		String fluxValue = "<projet>" + "\n" +
				"<status>1</status>" + "\n" +
				"<name>projet</name>" + "\n" +
				"</projet>" + "\n";
		userVarMap.put("$flux", fluxValue);
		repo.setUserVariables(userVarMap);

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
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);

		Map<String, Object> userVarMap = new HashMap<>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("<projet>").append("\n");
		fluxValue.append("<status>1</status>").append("\n");
		fluxValue.append("<name>projet</name>").append("\n");
		fluxValue.append("</projet>").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);

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
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);

		Map<String, Object> userVarMap = new HashMap<>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("{").append("\n");
		fluxValue.append("\"status\":\"1\",").append("\n");
		fluxValue.append("\"id\":\"1\",").append("\n");
		fluxValue.append("\"name\":\"projet\"}").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);

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
		IActionItemRepository repo = injector.getInstance(IActionItemRepository.class);
		
		Map<String, Object> userVarMap = new HashMap<>();
		StringBuilder userValue = new StringBuilder();
		userValue.append("{").append("\n");
		userValue.append("\"password\":\"mdp\",").append("\n");
		userValue.append("\"name\":\"user\"}").append("\n");
		userVarMap.put("$json", userValue.toString());
		userVarMap.put("$url", "https://www.google.com");
		repo.setUserVariables(userVarMap);

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
