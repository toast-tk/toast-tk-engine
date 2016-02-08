package com.synaptix.toast.core.report;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public class ErrorResult implements ITestResult{


	private String message;

	private String contextualTestSentence;

	private String screenShot;

	private ResultKind resultKind = ResultKind.ERROR;

	
	public ErrorResult() {
		this("KO");
	}

	public ErrorResult(
		String failureMessage) {
		this.message = failureMessage;
	}

	public ErrorResult(
		String message,
		String img) {
		this.message = message;
		this.setScreenShot(img);
		this.setResultKind(ResultKind.ERROR);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(
		String message) {
		this.message = message;
	}

	@Override
	public ResultKind getResultKind() {
		return resultKind;
	}

	public void setResultKind(
		ResultKind resultKind) {
		this.resultKind = resultKind;
	}

	public void setContextualTestSentence(
		String contextualTestSentence) {
		this.contextualTestSentence = contextualTestSentence;
	}

	public String getContextualTestSentence() {
		return contextualTestSentence;
	}

	public String getScreenShot() {
		return screenShot;
	}

	public void setScreenShot(
		String screenShot) {
		this.screenShot = screenShot;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public boolean isFailure() {
		return false;
	}

	@Override
	public boolean isError() {
		return true;
	}

	@Override
	public boolean isFatal() {
		return false;
	}
}