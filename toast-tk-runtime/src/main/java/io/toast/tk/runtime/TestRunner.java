package io.toast.tk.runtime;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;
import io.toast.tk.runtime.block.FatalExcecutionError;
import io.toast.tk.runtime.block.IBlockRunner;
import io.toast.tk.runtime.block.TestBlockRunner;

public class TestRunner {

	private static final Logger LOG = LogManager.getLogger(TestRunner.class);
	
	private boolean interupted = false;

	private int successNumber = 0;
	private int failureNumber = 0;
	
	private IBlockRunner blockRunner;
	
	@Inject
	private Map<Class, IBlockRunner> blockRunnerMap;

	/**
	 * Execute the different blocks within the test page
	 * 
	 * @return test page result
	 */
	public ITestPage run(final ITestPage testPage) {
		testPage.startExecution();
		try {
			runTestPageBlocks(testPage);
		} catch (FatalExcecutionError e) {
			LOG.error(e.getMessage(), e);
		}
		enrichTestPageResults(testPage);
		testPage.stopExecution();
		return testPage;
	}

	private ITestPage enrichTestPageResults(ITestPage testPage) {
		testPage.setTechnicalErrorNumber(0);
		testPage.setTestSuccessNumber(0);
		testPage.setTestFailureNumber(0);
		for (final IBlock block : testPage.getBlocks()) {
			if (block instanceof ITestPage) {
				final ITestPage subPage = enrichTestPageResults((ITestPage) block);
				testPage.setTechnicalErrorNumber(
						testPage.getTechnicalErrorNumber() + subPage.getTechnicalErrorNumber());
				testPage.setTestFailureNumber(testPage.getTestFailureNumber() + subPage.getTestFailureNumber());
				testPage.setTestSuccessNumber(testPage.getTestSuccessNumber() + subPage.getTestSuccessNumber());
				testPage.setIsSuccess(testPage.getTestFailureNumber() + testPage.getTechnicalErrorNumber() == 0);
			} else if (block instanceof TestBlock) {
				final TestBlock testBlock = (TestBlock) block;
				enrichTestBlockResults(testPage, testBlock);
			}
		}
		testPage.setIsSuccess(testPage.getTestFailureNumber() + testPage.getTechnicalErrorNumber() == 0);
		return testPage;
	}

	private void enrichTestBlockResults(ITestPage testPage, TestBlock testBlock) {
		int nbBlockSuccess = 0;
		int nbBlockFailures = 0;
		int nbBlockErrors = 0;
		for (TestLine line : testBlock.getBlockLines()) {
			ITestResult result = line.getTestResult();
			if (result != null) {
				testPage.setTestSuccessNumber(
						result.isSuccess() ? testPage.getTestSuccessNumber() + 1 : testPage.getTestSuccessNumber());
				nbBlockSuccess += result.isSuccess() ? 1 : 0;
				testPage.setTestFailureNumber(
						result.isFailure() ? testPage.getTestFailureNumber() + 1 : testPage.getTestFailureNumber());
				nbBlockFailures += result.isFailure() ? 1 : 0;
				testPage.setTechnicalErrorNumber(
						result.isError() ? testPage.getTechnicalErrorNumber() + 1 : testPage.getTechnicalErrorNumber());
				nbBlockErrors += result.isError() ? 1 : 0;
				if (result.isFatal()) {
					testPage.setIsFatal(true);
				}
			}
		}
		testBlock.setTechnicalErrorNumber(nbBlockErrors);
		testBlock.setTestSuccessNumber(nbBlockSuccess);
		testBlock.setTestFailureNumber(nbBlockFailures);

	}
	
	public void kill() {
		this.interupted = true;
		if(blockRunner != null && blockRunner instanceof TestBlockRunner) {
			((TestBlockRunner) blockRunner).kill();		
		}
	}

	private void runTestPageBlocks(final ITestPage testPage) {
		for (final IBlock block : testPage.getBlocks()) {
			if(interupted) { break; }
			
			if (block instanceof ITestPage) {
				run((ITestPage) block);
			} else {
				runBlocks(block);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void runBlocks(IBlock block) {
		blockRunner = blockRunnerMap.get(block.getClass());
		if (blockRunner == null) {
			LOG.debug("No runner found for block of type " + block.getClass().getSimpleName());
		} else if(blockRunner instanceof TestBlockRunner) {
			((TestBlockRunner) blockRunner).initializeNumber();
			
			blockRunner.run(block);
			
			// Used for the agent
			successNumber += ((TestBlockRunner) blockRunner).getSuccessNumber();
			failureNumber += ((TestBlockRunner) blockRunner).getFailureNumber();
		} else {
			blockRunner.run(block);
		}
	}

	public int getSuccessNumber() {
		int res = successNumber;
		
		// If a block is still running
		if(blockRunner instanceof TestBlockRunner) {
			res = res + ((TestBlockRunner) blockRunner).getSuccessNumber();
		}
		return res;
	}

	public int getFailureNumber() {
		int res = failureNumber;
		
		// If a block is still running
		if(blockRunner instanceof TestBlockRunner) {
			res = res + ((TestBlockRunner) blockRunner).getFailureNumber();
		}
		return res;
	}
}