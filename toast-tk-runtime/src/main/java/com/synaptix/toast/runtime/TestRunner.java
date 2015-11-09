package com.synaptix.toast.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.synaptix.toast.core.annotation.EngineEventBus;
import com.synaptix.toast.core.event.TestProgressMessage;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.block.BlockRunnerProvider;
import com.synaptix.toast.runtime.block.IBlockRunner;

class TestRunner {

	private static final Logger LOG = LogManager.getLogger(TestRunner.class);
	private BlockRunnerProvider blockRunnerProvider;
	private Injector injector;
	private EventBus eventBus;

	public TestRunner(Injector injector){
		this.setInjector(injector);
	}

	private void setInjector(
		Injector injector) {
		this.injector = injector;
		this.blockRunnerProvider = injector.getInstance(BlockRunnerProvider.class);
		this.eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
	}

	/**
	 * Execute the different blocks within the test page
	 * 
	 * @return test page result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ITestPage run(
		ITestPage testPage,
		boolean inlineReport)
		throws IllegalAccessException, ClassNotFoundException {
		testPage.startExecution();
		for(IBlock block : testPage.getBlocks()) {
			if(block instanceof ITestPage){
				run((ITestPage)block, inlineReport);
				if(inlineReport){
					eventBus.post(new TestProgressMessage(testPage));
				}
			}else{
				IBlockRunner blockRunner = blockRunnerProvider.getBlockRunner(block.getClass(), injector);
				if(blockRunner != null){
					blockRunner.run(block);
				}
			}
		}
		testPage.stopExecution();
		return testPage;
	}

}