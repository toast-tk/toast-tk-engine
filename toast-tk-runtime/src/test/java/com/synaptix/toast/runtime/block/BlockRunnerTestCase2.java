package com.synaptix.toast.runtime.block;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.synaptix.toast.adapter.cache.ToastCache;
import com.synaptix.toast.core.annotation.EngineEventBus;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.AbstractScenarioRunner;
import com.synaptix.toast.runtime.ActionItemRepository;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;
import com.synaptix.toast.runtime.parse.ScriptHelper;
import com.synaptix.toast.runtime.parse.TestParser;
import com.synaptix.toast.test.runtime.resource.XmlAdapterExample;

public class BlockRunnerTestCase2 {

	private static final String scenario = "./fatal.scenario.md";
	private ITestPage testPage;
	
	@BeforeClass
	public static void init() {
		ToastCache.getInstance().addActionAdapter(XmlAdapterExample.class);
	}
	
	@Before
	public void initRunner() throws IllegalArgumentException, IOException{
        this.testPage = new TestParser().parse(ScriptHelper.getScript(scenario), scenario);
	}

	@Test
	public void testParserBlocks(){
        Assert.assertEquals(testPage.getBlocks().size(), 1);
	}
	
	@Test
	public void testParserLines(){
        TestBlock block = (TestBlock) testPage.getBlocks().get(0);
        Assert.assertEquals(block.getBlockLines().size(), 3);
	}
	
	@Test
	public void testNotFailCommand() throws IOException{
        TestBlock block = (TestBlock) testPage.getBlocks().get(0);
        Assert.assertEquals(block.getBlockLines().size(), 3);
        TestLine line = block.getBlockLines().get(0);
        TestLineDescriptor descriptor = new TestLineDescriptor(block, line);
        Assert.assertEquals(descriptor.isFailFatalCommand(), false);
	}
	
	@Test
	public void testFailCommand() throws IOException{
        TestBlock block = (TestBlock) testPage.getBlocks().get(0);
        Assert.assertEquals(block.getBlockLines().size(), 3);
        TestLine line = block.getBlockLines().get(1);
        TestLineDescriptor descriptor = new TestLineDescriptor(block, line);
        Assert.assertEquals(descriptor.isFailFatalCommand(), true);
	}
	
	@Test
	public void testScenarioExecution() throws Exception{
		TestScenarioRunner runner = new TestScenarioRunner();
		runner.run(scenario);
	}
	
	private class TestScenarioRunner extends AbstractScenarioRunner{

		@Override
		public void tearDownEnvironment() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beginTest() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endTest() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void initEnvironment() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
