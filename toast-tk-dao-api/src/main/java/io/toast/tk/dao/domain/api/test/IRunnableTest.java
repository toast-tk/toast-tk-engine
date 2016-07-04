package io.toast.tk.dao.domain.api.test;

import io.toast.tk.dao.core.report.TestResult;

public interface IRunnableTest {

	TestResult getTestResult();

	void setTestResult(final TestResult testResult);

	long getExecutionTime();
}