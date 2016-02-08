package com.synaptix.toast.core.runtime;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public class ErrorResultReceivedException extends Exception {

	private static final long serialVersionUID = 1L;

	private ITestResult result;

	public ErrorResultReceivedException(
			ITestResult result) {
		super();
		this.result = result;
	}

	public ITestResult getResult() {
		return result;
	}
}
