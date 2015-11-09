package com.synaptix.toast.core.report;

import java.awt.image.BufferedImage;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public class TestResult implements ITestResult{

	private final boolean isSuccess;

	private String message;

	private String contextualTestSentence;

	private BufferedImage screenShot;

	// expected to add
	private ResultKind resultKind;

	/**
	 * This constructor can be used in case of success.
	 */
	public TestResult() {
		this("OK", ResultKind.SUCCESS);
	}

	/**
	 * This constructor will create a TestResult of kind FAILURE (i.e. a test has failed, but it is not a technical error).
	 * 
	 * @param failureMessage
	 */
	public TestResult(
		String failureMessage) {
		this(failureMessage, ResultKind.FAILURE);
	}

// public TestResult(String failureMessage, BufferedImage img) {
// this(failureMessage, ResultKind.FAILURE, img);
// }
//
	public TestResult(
		String message,
		ResultKind resultKind,
		BufferedImage img) {
		this.message = message;
		this.isSuccess = resultKind.equals(ResultKind.SUCCESS) || resultKind.equals(ResultKind.INFO);
		this.setScreenShot(img);
		this.setResultKind(resultKind);
	}

	public TestResult(
		String message,
		ResultKind resultKind) {
		this(message, resultKind, null);
	}

	public boolean isSuccess() {
		return ResultKind.SUCCESS.equals(resultKind) || ResultKind.INFO.equals(resultKind);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(
		String message) {
		this.message = message;
	}

	public ResultKind getResultKind() {
		return resultKind;
	}

	public void setResultKind(
		ResultKind resultKind) {
		this.resultKind = resultKind;
	}

	public enum ResultKind {
		/**
		 * Fatal error, stops execution (red)
		 */
		FATAL,
		/**
		 * Test failure (red)
		 */
		FAILURE,
		/**
		 * Technical error (yellow)
		 */
		ERROR,
		/**
		 * Test success (green)
		 */
		SUCCESS,
		/**
		 * Technical success, or info (blue)
		 */
		INFO
	}

	public void setContextualTestSentence(
		String contextualTestSentence) {
		this.contextualTestSentence = contextualTestSentence;
	}

	public String getContextualTestSentence() {
		return contextualTestSentence;
	}

	public BufferedImage getScreenShot() {
		return screenShot;
	}

	public void setScreenShot(
		BufferedImage screenShot) {
		this.screenShot = screenShot;
	}
}