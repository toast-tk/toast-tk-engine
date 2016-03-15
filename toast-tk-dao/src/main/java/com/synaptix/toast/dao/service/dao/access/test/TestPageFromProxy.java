package com.synaptix.toast.dao.service.dao.access.test;

import java.util.ArrayList;
import java.util.List;

import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;

public class TestPageFromProxy {

	public static final TestPage from(ITestPage testPage){
		TestPage page = new TestPage();
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
		List<IBlock> blocks = page.getBlocks();
		ArrayList<IBlock> out = new ArrayList<IBlock>(blocks.size());
		for (IBlock iBlock : blocks) {
			if(iBlock instanceof ITestPage){
				out.add(TestPageFromProxy.from((ITestPage)iBlock));
			}else{
				out.add(iBlock);
			}
		}
		return page;
	}
	
}
