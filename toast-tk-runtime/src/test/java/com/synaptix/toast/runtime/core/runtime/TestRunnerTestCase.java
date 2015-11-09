package com.synaptix.toast.runtime.core.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Test;

import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.block.TestBlockRunner;
import com.synaptix.toast.runtime.utils.ArgumentHelper;
import com.synaptix.toast.test.runtime.mock.DefaultRepositorySetup;

public class TestRunnerTestCase {

	class Tata {
	}

	class Toto extends Tata {
	}

	class Titi {
		@Action(action = "Titi", description = "")
		public void blabla() {
		}
	}

	@Test
	public void testEmptyResult()
		throws IOException {
		TestBlockRunner runner = new TestBlockRunner();
		ActionCommandDescriptor findMethodInClass = runner.findMethodInClass("Titi", Toto.class);
		assertNull(findMethodInClass);
	}

	@Test
	public void testNonEmptyResult()
		throws IOException {
		TestBlockRunner runner = new TestBlockRunner();
		ActionCommandDescriptor findMethodInClass = runner.findMethodInClass("Titi", Titi.class);
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
		String convertActionSentenceToRegex = ArgumentHelper
			.convertActionSentenceToRegex("Faire action sur {{champ:variable:string}}");
		assertEquals("Faire action sur {{champ:variable:string}}", convertActionSentenceToRegex);
	}

	@Test
	public void testActionItemVariableDefaultReplacement()
		throws IOException {
		String convertActionSentenceToRegex = ArgumentHelper
			.convertActionSentenceToRegex("Faire action sur {{value}}");
		assertEquals("Faire action sur \\*([\\$?\\w\\W]+)\\*", convertActionSentenceToRegex);
	}

	@AfterClass
	public static void end() {
	}
}
