package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;
import com.synaptix.toast.dao.domain.api.test.IRunnableTest;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.impl.common.BasicEntityBean;
import com.synaptix.toast.dao.domain.impl.repository.ReportHelper;

@Entity(value = "test")
@Indexes({
        @Index(value = "pageName, -runDateTime"), 
        @Index("runDateTime"), 
        @Index("isTemplate")
})
public class TestPage extends BasicEntityBean implements IRunnableTest, ITestPage {

    @Embedded
    private ITestResult testResult;

    @Embedded
    private List<IBlock> blocks;

    private int technicalErrorNumber;

    private int testSuccessNumber;

    private int testFailureNumber;

    private String parsingErrorMessage;

    private long runDateTime;

    private long executionTime;

    private long previousExecutionTime;

    private boolean previousIsSuccess;

    private boolean isTemplate;

    public TestPage() {
    	this.blocks = new ArrayList<>();
    }

    public int getTechnicalErrorNumber() {
        return technicalErrorNumber;
    }

    public void setTechnicalErrorNumber(
            int technicalErrorNumber) {
        this.technicalErrorNumber = technicalErrorNumber;
    }

    public int getTestSuccessNumber() {
        return testSuccessNumber;
    }

    public void setTestSuccessNumber(
            int testSuccessNumber) {
        this.testSuccessNumber = testSuccessNumber;
    }

    public int getTestFailureNumber() {
        return testFailureNumber;
    }

    public void setTestFailureNumber(
            int testFailureNumber) {
        this.testFailureNumber = testFailureNumber;
    }

    public void addBlock(
            IBlock testBlock) {
        blocks.add(testBlock);
    }

    public String getParsingErrorMessage() {
        return parsingErrorMessage;
    }

    public void setParsingErrorMessage(
            String parsingErrorMessage) {
        this.parsingErrorMessage = parsingErrorMessage;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(
            long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public ITestResult getTestResult() {
        return this.testResult;
    }

    @Override
    public void setTestResult(
            ITestResult testResult) {
        this.testResult = testResult;
    }

    @Override
    public void startExecution() {
        this.runDateTime = System.currentTimeMillis();
        setPreviousIsSuccess(ReportHelper.isSuccess(this));
        previousExecutionTime = executionTime;
    }

    @Override
    public void stopExecution() {
        this.executionTime = System.currentTimeMillis() - runDateTime;
    }


    public List<IBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(
            List<IBlock> blocks) {
        this.blocks = blocks;
    }

    @Override
    public long getPreviousExecutionTime() {
        return previousExecutionTime;
    }

    @Override
    public void setPreviousExecutionTime(
            long previousExecutionTime) {
        this.previousExecutionTime = previousExecutionTime;
    }

    @Override
    public boolean isPreviousIsSuccess() {
        return previousIsSuccess;
    }

    @Override
    public void setPreviousIsSuccess(
            boolean previousIsSuccess) {
        this.previousIsSuccess = previousIsSuccess;
    }

    public void setIsTemplate(
            boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public boolean getIsTemplate() {
        return this.isTemplate;
    }

    @Override
    public String getBlockType() {
        return "testPageBlock";
    }


	@Override
	public long getStartDateTime() {
		return runDateTime;
	}

	@Override
	public int getHeaderSize() {
		return 0;
	}

}
