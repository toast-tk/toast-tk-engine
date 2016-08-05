package io.toast.tk.dao.domain.impl.test.block;

import java.util.List;

import io.toast.tk.dao.core.report.TestResult;

public interface ITestPage extends IBlock {

    List<IBlock> getBlocks();

    long getExecutionTime();

    int getTechnicalErrorNumber();

    int getTestFailureNumber();

    int getTestSuccessNumber();

    void setTechnicalErrorNumber(final int technicalErrorNumber);

    void setTestFailureNumber(final int testFailureNumber);

    void setTestSuccessNumber(final int testSuccessNumber);

    String getName();

    void setId(final String id);

    boolean isPreviousIsSuccess();

    void setPreviousIsSuccess(final boolean isSuccess);

    long getPreviousExecutionTime();

    void startExecution();

    void stopExecution();

    String getParsingErrorMessage();

    String getIdAsString();

    void setPreviousExecutionTime(final long previousExecutionTime);

    long getStartDateTime();

    void setName(final String name);

    void addBlock(final IBlock block);

    TestResult getTestResult();
	
	public boolean isSuccess();

	void setIsSuccess(final boolean isSuccess);

	void setIsFatal(boolean isFatal);

	boolean isFatal();
}
