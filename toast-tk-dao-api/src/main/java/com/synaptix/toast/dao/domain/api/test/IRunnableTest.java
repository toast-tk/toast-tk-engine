package com.synaptix.toast.dao.domain.api.test;

import com.synaptix.toast.core.report.TestResult;

public interface IRunnableTest {

	TestResult getTestResult();

	void setTestResult(final TestResult testResult);

	long getExecutionTime();
}