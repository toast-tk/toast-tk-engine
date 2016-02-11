package com.synaptix.toast.dao.service.dao.access.test;

import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;

public class TestPageFromProxy {

	public static final TestPage from(final ITestPage testPage){
		final TestPage page = new TestPage();
		page.setName(testPage.getName());
		page.setExecutionTime(testPage.getExecutionTime());
		page.setBlocks(testPage.getBlocks());
		page.setId(testPage.getIdAsString());
		page.setIsSuccess(testPage.isSuccess());
		page.setPreviousIsSuccess(testPage.isPreviousIsSuccess());
		page.setParsingErrorMessage(testPage.getParsingErrorMessage());
		page.setPreviousExecutionTime(testPage.getPreviousExecutionTime());
		page.setTechnicalErrorNumber(testPage.getTechnicalErrorNumber());
		page.setTestFailureNumber(testPage.getTestFailureNumber());
		page.setTestResult(testPage.getTestResult());
		page.setTestSuccessNumber(testPage.getTestSuccessNumber());
		return page;
	}
}