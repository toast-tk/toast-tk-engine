package io.toast.tk.runtime.block;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.toast.tk.runtime.parse.FileHelper;

import io.toast.tk.adapter.cache.ToastCache;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;
import io.toast.tk.runtime.AbstractScenarioRunner;
import io.toast.tk.runtime.bean.TestLineDescriptor;
import io.toast.tk.runtime.parse.TestParser;
import io.toast.tk.test.runtime.resource.XmlAdapterExample;

public class BlockRunnerTestCase2 {

	private static final String scenario = "./fatal.scenario.md";
	private ITestPage testPage;
	
	@BeforeClass
	public static void init() {
		ToastCache.getInstance().addActionAdapter(XmlAdapterExample.class);
	}
	
	@Before
	public void initRunner() throws IllegalArgumentException, IOException{
        this.testPage = new TestParser().parse(FileHelper.getScript(scenario), scenario);
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
