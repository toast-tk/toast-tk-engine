package com.synaptix.toast.core.report;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public class TestResult implements ITestResult{

	protected String message;

	protected String contextualTestSentence;

	protected String screenShot;

	protected ResultKind resultKind;

	protected boolean isSuccess;

	protected boolean isFailure;

	protected boolean isError;


	public TestResult() {
		this("OK");
	}

	/**
	 * This constructor will create a TestResult of kind FAILURE (i.e. a test has failed, but it is not a technical error).
	 * 
	 * @param failureMessage
	 */
	public TestResult(final String message) {
		this.message = message;
	}

	public TestResult(
		final String message,
		final String img
	) {
		this.message = message;
		this.setScreenShot(img);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public void setResultKind(final ResultKind resultKind) {
		this.resultKind = resultKind;
	}

	@Override
	public void setContextualTestSentence(final String contextualTestSentence) {
		this.contextualTestSentence = contextualTestSentence;
	}

	@Override
	public String getContextualTestSentence() {
		return contextualTestSentence;
	}

	@Override
	public String getScreenShot() {
		return screenShot;
	}

	@Override
	public void setScreenShot(final String screenShot) {
		this.screenShot = screenShot;
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public boolean isFailure() {
		return isFailure;
	}

	@Override
	public boolean isError() {
		return isError;
	}

	@Override
	public boolean isFatal() {
		return ResultKind.FATAL.equals(resultKind);
	}

	@Override
	public ResultKind getResultKind() {
		return resultKind;
	}

	@Override
	public void setIsSuccess(final boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	@Override
	public void setIsFailure(final boolean isFailure) {
		this.isFailure = isFailure;
	}

	@Override
	public void setIsError(final boolean isError) {
		this.isError = isError;
	}
}