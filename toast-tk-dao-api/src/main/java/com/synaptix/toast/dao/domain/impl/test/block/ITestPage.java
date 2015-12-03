package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.List;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface ITestPage extends ITaggable, IBlock {

    List<IBlock> getBlocks();

    long getExecutionTime();

    int getTechnicalErrorNumber();

    int getTestFailureNumber();

    int getTestSuccessNumber();

    void setTechnicalErrorNumber(int technicalErrorNumber);

    void setTestFailureNumber(int testFailureNumber);

    void setTestSuccessNumber(int testSuccessNumber);

    String getName();

    void setId(String id);

    boolean isPreviousIsSuccess();

    void setPreviousIsSuccess(boolean isSuccess);

    long getPreviousExecutionTime();

    void startExecution();

    void stopExecution();

    String getParsingErrorMessage();

    String getIdAsString();

    void setPreviousExecutionTime(long previousExecutionTime);

    long getStartDateTime();

    void setName(String name);

    void addBlock(IBlock block);

	ITestResult getTestResult();
	
	public boolean isSuccess();

	void setIsSuccess(boolean isSuccess);
}
