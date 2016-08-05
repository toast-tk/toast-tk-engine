package io.toast.tk.core.runtime;

import io.toast.tk.dao.core.report.TestResult;

public class ErrorResultReceivedException extends Exception {

	private static final long serialVersionUID = -91654577486978769L;
	private TestResult result;

	public ErrorResultReceivedException(final TestResult result) {
		this.result = result;
	}

	public TestResult getResult() {
		return result;
	}
}