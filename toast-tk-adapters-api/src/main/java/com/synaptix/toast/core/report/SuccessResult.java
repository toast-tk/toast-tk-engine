package com.synaptix.toast.core.report;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public class SuccessResult implements ITestResult{

	private String message;

	private String contextualTestSentence;

	private String screenShot;

	private ResultKind resultKind = ResultKind.SUCCESS;

	public SuccessResult() {
		this("OK");
	}

	/**
	 * This constructor will create a TestResult of kind FAILURE (i.e. a test has failed, but it is not a technical error).
	 * 
	 * @param failureMessage
	 */
	public SuccessResult(
		String message) {
		this.message = message;
	}

	public SuccessResult(
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
		return true;
	}

	@Override
	public boolean isFailure() {
		return false;
	}

	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public boolean isFatal() {
		return false;
	}

	@Override
	public ResultKind getResultKind() {
		return resultKind;
	}
}