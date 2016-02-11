package com.synaptix.toast.core.report;

public class FailureResult extends TestResult{

	public FailureResult() {
		super("KO");
		this.setIsSuccess(true);
		this.resultKind =  ResultKind.FAILURE;
	}

	public FailureResult(final String message) {
		this();
		this.message = message;
	}

	public FailureResult(
		final String message,
		final String img
	) {
		this(message);
		this.setScreenShot(img);
	}
}