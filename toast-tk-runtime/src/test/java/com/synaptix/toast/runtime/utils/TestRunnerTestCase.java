package com.synaptix.toast.runtime.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.synaptix.toast.adapter.cache.ToastCache;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.block.BlockRunnerTestCase;
import com.synaptix.toast.runtime.block.TestBlockRunner;
import com.synaptix.toast.test.runtime.mock.DefaultRepositorySetup;
import com.synaptix.toast.test.runtime.resource.JsonAdapterExample;
import com.synaptix.toast.test.runtime.resource.XmlAdapterExample;

public class TestRunnerTestCase {

	@BeforeClass
	public static void init() {
		ToastCache.getInstance().addActionAdapter(Titi.class);
		ToastCache.getInstance().addActionAdapter(Toto.class);
	}
	
	@Test
	public void testEmptyResult() throws IOException {
		TestBlockRunner runner = new TestBlockRunner();
		ActionCommandDescriptor findMethodInClass = runner.findMatchingAction("Titi", Toto.class);
		assertNull(findMethodInClass);
	}

	@Test
	public void testNonEmptyResult()
		throws IOException {
		TestBlockRunner runner = new TestBlockRunner();
		ActionCommandDescriptor findMethodInClass = runner.findMatchingAction("Titi", Titi.class);
		assertNotNull(findMethodInClass);
	}

	@Test
	public void testArgumentBuild() {
		IActionItemRepository repo = new DefaultRepositorySetup();
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		userVarMap.put("$variable", "200");
		repo.setUserVariables(userVarMap);
		Object buildArgument = ArgumentHelper.buildActionAdapterArgument(repo, "$variable");
		assertEquals(buildArgument, "200");
		buildArgument = ArgumentHelper.buildActionAdapterArgument(repo, "*$variable*");
		assertEquals(buildArgument, "200");
		buildArgument = ArgumentHelper.buildActionAdapterArgument(repo, "$variables");
		assertEquals(buildArgument, "$variables");
		buildArgument = ArgumentHelper.buildActionAdapterArgument(repo, "*variable*");
		assertEquals(buildArgument, "variable");
	}

	@Test
	public void testComplexArgumentBuild() {
		IActionItemRepository repo = new DefaultRepositorySetup();
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		userVarMap.put("$var", "value");
		userVarMap.put("$variable", "nested $var replacement");
		repo.setUserVariables(userVarMap);
		Object buildArgument = ArgumentHelper.buildActionAdapterArgument(repo, "$variable");
		assertEquals(buildArgument, "nested value replacement");
	}

	@Test
	public void testComplexMultipleArgumentBuild() {
		IActionItemRepository repo = new DefaultRepositorySetup();
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		userVarMap.put("$var", "value");
		userVarMap.put("$vari", "value");
		userVarMap.put("$variable", "nested $var replacement \n with another $vari");
		repo.setUserVariables(userVarMap);
		Object buildArgument = ArgumentHelper
			.buildActionAdapterArgument(repo, "$variable");
		assertEquals(buildArgument, "nested value replacement \n with another value");
	}

	@Test
	public void testActionItemVariableReplacement()
		throws IOException {
		CommandArgumentDescriptor descriptor = ArgumentHelper
			.convertActionSentenceToRegex("Faire action sur {{champ:variable:string}}");
		assertEquals("Faire action sur {{champ:variable:string}}", descriptor.regex);
	}

	@Test
	public void testActionItemVariableDefaultReplacement()
		throws IOException {
		CommandArgumentDescriptor descriptor = ArgumentHelper
			.convertActionSentenceToRegex("Faire action sur {{value}}");
		assertEquals("Faire action sur \\*([^\\*]+)\\*", descriptor.regex);
	}

	@AfterClass
	public static void end() {
	}
}
