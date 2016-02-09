package com.synaptix.toast.core.report;

import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;


public class TestResult implements ITestResult{

	protected String message;

	protected String contextualTestSentence;

	protected String screenShot;

	protected ResultKind resultKind;

	protected boolean isSuccess;

	protected boolean isFailure;

	protected boolean isError;

	protected boolean isFatal;

	public TestResult() {
		this("OK");
	}

	/**
	 * This constructor will create a TestResult of kind FAILURE (i.e. a test has failed, but it is not a technical error).
	 * 
	 * @param failureMessage
	 */
	public TestResult(String message) {
		this.message = message;
	}

	public TestResult(
		String message,
		String img) {
		this.message = message;
		this.setScreenShot(img);
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
		return isFatal;
	}

	@Override
	public ResultKind getResultKind() {
		return resultKind;
	}

	@Override
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	@Override
	public void setIsFailure(boolean isFailure) {
		this.isFailure = isFailure;
	}

	@Override
	public void setIsError(boolean isError) {
		this.isError = isError;
	}
}