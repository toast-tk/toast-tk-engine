package com.synaptix.toast.test.runtime;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.block.TestBlockRunner;
import com.synaptix.toast.runtime.utils.ArgumentHelper;
import com.synaptix.toast.test.bean.XmlAdapterExample;

public class TestParserTestCase_2 {

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
		String sentenceAsRegex = ArgumentHelper.convertActionSentenceToRegex("{{com.synaptix.toast.test.bean.ProjetFlux:value:xml}}");
		Assert.assertEquals("\\*([\\$?\\w\\W]+)\\*", sentenceAsRegex);
	}
	
	@Test
	public void testParserMethodFinder() {
		TestBlockRunner blockRunner = new TestBlockRunner();
		ActionCommandDescriptor method = blockRunner.findMethodInClass("Intégrer *$flux*", XmlAdapterExample.class);
		Assert.assertNotNull(method);
	}
}
