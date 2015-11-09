package com.synaptix.toast.dao.domain.api.test;

public interface IRunnableTest {

	public ITestResult getTestResult();

	public void setTestResult(
			ITestResult testResult);

	public long getExecutionTime();

}
