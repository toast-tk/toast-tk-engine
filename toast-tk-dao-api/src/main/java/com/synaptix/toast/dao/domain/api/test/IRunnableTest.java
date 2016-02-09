package com.synaptix.toast.dao.domain.api.test;

import com.synaptix.toast.core.report.TestResult;

public interface IRunnableTest {

	public TestResult getTestResult();

	public void setTestResult(TestResult testResult);

	public long getExecutionTime();

}
