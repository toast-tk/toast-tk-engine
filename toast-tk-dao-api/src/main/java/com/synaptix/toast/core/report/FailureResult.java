package com.synaptix.toast.core.report;



public class FailureResult extends TestResult{

	public FailureResult() {
		super("KO");
		this.setIsFailure(true);
		this.resultKind =  ResultKind.FAILURE;

	}

	public FailureResult(
		String message) {
		this();
		this.message = message;
	}

	public FailureResult(
		String message,
		String img) {
		this(message);
		this.setScreenShot(img);
	}
}