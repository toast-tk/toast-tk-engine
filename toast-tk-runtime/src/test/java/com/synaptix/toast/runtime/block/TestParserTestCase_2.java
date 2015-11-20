package com.synaptix.toast.runtime.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.synaptix.toast.adapter.swing.AbstractSwingActionAdapter;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.block.TestBlockRunner;
import com.synaptix.toast.runtime.utils.ArgumentHelper;
import com.synaptix.toast.test.runtime.mock.DefaultRepositorySetup;
import com.synaptix.toast.test.runtime.resource.HttpAdapterExample;
import com.synaptix.toast.test.runtime.resource.JsonAdapterExample;
import com.synaptix.toast.test.runtime.resource.XmlAdapterExample;

public class TestParserTestCase_2 {

	private static final Logger LOG = LogManager.getLogger(TestParserTestCase_2.class);
	static String scenario;

	@BeforeClass
	public static void init() {
		InputStream stream = TestParserTestCase_2.class.getClassLoader().getResourceAsStream("./flux.scenario.txt");
		try{
			scenario = IOUtils.toString(stream);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Test
	public void testParserArgument() {
		CommandArgumentDescriptor descriptor = ArgumentHelper.convertActionSentenceToRegex("{{com.synaptix.toast.test.runtime.resource.ProjetFlux:value:xml}}");
		Assert.assertEquals("\\*([\\$?\\w\\W]+)\\*", descriptor.command);
		Assert.assertEquals(descriptor.arguments.size(), 1);
	}
	
	
	@Test
	public void testDialogMethod(){
		TestBlockRunner blockRunner = new TestBlockRunner();
		ActionCommandDescriptor method = blockRunner.findMethodInClass("Affichage dialogue *Choix service*", AbstractSwingActionAdapter.class);
		Assert.assertNotNull(method);
	}
	
	
	@Test
	public void testWaitMethod(){
		TestBlockRunner blockRunner = new TestBlockRunner();
		ActionCommandDescriptor method = blockRunner.findMethodInClass("wait for *10* sec", AbstractSwingActionAdapter.class);
		Assert.assertNotNull(method);
	}
	
	
	
	@Test
	public void testParserMethodFinder() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		ActionCommandDescriptor method = blockRunner.findMethodInClass("Intégrer *$flux*", XmlAdapterExample.class);
		Assert.assertNotNull(method);
	}
	
	
	@Test
	public void testReverseEngineeringMethodParamType() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = new ObjectRepositorySetup();
		
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>").append("\n");
		fluxValue.append("<projet>").append("\n");
		fluxValue.append("<status>1</status>").append("\n");
		fluxValue.append("<name>projet</name>").append("\n");
		fluxValue.append("</projet>").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);
		
		blockRunner.setObjectRepository(repo);
		
		ActionCommandDescriptor method = blockRunner.findMethodInClass("Integrate *$flux*", XmlAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 1);
	}
	
	@Test
	public void testRunnerArgumentBuilderXml() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = new ObjectRepositorySetup();
		
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>").append("\n");
		fluxValue.append("<projet>").append("\n");
		fluxValue.append("<status>1</status>").append("\n");
		fluxValue.append("<name>projet</name>").append("\n");
		fluxValue.append("</projet>").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);
		
		blockRunner.setObjectRepository(repo);
		
		ActionCommandDescriptor method = blockRunner.findMethodInClass("Intégrer *$flux*", XmlAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 1);
	}
	
	
	@Test
	public void testRunnerArgumentBuilderJson() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = new ObjectRepositorySetup();
		
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		StringBuilder fluxValue = new StringBuilder();
		fluxValue.append("{").append("\n");
		fluxValue.append("\"status\":\"1\",").append("\n");
		fluxValue.append("\"id\":\"1\",").append("\n");
		fluxValue.append("\"name\":\"projet\"}").append("\n");
		userVarMap.put("$flux", fluxValue.toString());
		repo.setUserVariables(userVarMap);
		
		blockRunner.setObjectRepository(repo);
		
		ActionCommandDescriptor method = blockRunner.findMethodInClass("Intégrer *$flux*", JsonAdapterExample.class);
		Object[] args = null;
		try {
			args = blockRunner.buildArgumentList(method);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(args);
		Assert.assertEquals(args.length, 1);
	}
	
	@Test
	public void testRunnerArgumentBuilderJsonAndVar() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		IActionItemRepository repo = new ObjectRepositorySetup();
		
		Map<String, Object> userVarMap = new HashMap<String, Object>();
		StringBuilder userValue = new StringBuilder();
		userValue.append("{").append("\n");
		userValue.append("\"password\":\"mdp\",").append("\n");
		userValue.append("\"name\":\"user\"}").append("\n");
		userVarMap.put("$json", userValue.toString());
		userVarMap.put("$url", "https://www.google.com");
		repo.setUserVariables(userVarMap);
		
		blockRunner.setObjectRepository(repo);
		
		ActionCommandDescriptor method = blockRunner.findMethodInClass("POST *$json* to *$url*", HttpAdapterExample.class);
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
