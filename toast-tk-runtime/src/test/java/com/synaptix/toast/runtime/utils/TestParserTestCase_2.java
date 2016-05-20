package com.synaptix.toast.runtime.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;

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
		CommandArgumentDescriptor descriptor = ArgumentHelper.convertActionSentenceToRegex("{{com.synaptix.toast.test.runtime.resource.ProjetFlux:value:xml}}");
		Assert.assertEquals("\\*([\\$?\\w\\W][^\\*]+)\\*", descriptor.regex);
		Assert.assertEquals(descriptor.arguments.size(), 1);
	}
	

}
