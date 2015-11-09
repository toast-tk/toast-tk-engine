package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.List;

public interface ITestPage extends ITaggable, IBlock {

	public List<IBlock> getBlocks();

	public long getExecutionTime();

	public int getTechnicalErrorNumber();

	public int getTestFailureNumber();

	public int getTestSuccessNumber();

	public void setTechnicalErrorNumber(
		int technicalErrorNumber);

	public void setTestFailureNumber(
		int testFailureNumber);

	public void setTestSuccessNumber(
		int testSuccessNumber);

	public String getName();

	public void setId(
		String id);

	public boolean isPreviousIsSuccess();
	
	public void setPreviousIsSuccess(boolean isSuccess);

	public long getPreviousExecutionTime();

	public void startExecution();

	public void stopExecution();

	public Object getParsingErrorMessage();

	public String getIdAsString();

	void setPreviousExecutionTime(
		long previousExecutionTime);

	long getStartDateTime();

	public void setName(String name);

	public void addBlock(IBlock block);
}
