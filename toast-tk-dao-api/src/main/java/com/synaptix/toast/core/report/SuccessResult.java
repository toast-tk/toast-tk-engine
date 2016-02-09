package com.synaptix.toast.core.report;



public class SuccessResult extends TestResult{

	public SuccessResult() {
		super("OK");
		this.setIsSuccess(true);
		this.resultKind =  ResultKind.SUCCESS;

	}

	public SuccessResult(
		String message) {
		this();
		this.message = message;
	}

	public SuccessResult(
		String message,
		String img) {
		this(message);
		this.setScreenShot(img);
	}

}