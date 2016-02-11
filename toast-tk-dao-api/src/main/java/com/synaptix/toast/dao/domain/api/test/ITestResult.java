package com.synaptix.toast.dao.domain.api.test;

public interface ITestResult {
	
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

	String getContextualTestSentence();

	String getMessage();
	
	boolean isSuccess();
	
	boolean isFailure();

	boolean isError();

	void setIsSuccess(final boolean isSuccess);
	
	void setIsFailure(final boolean isFailure);

	void setIsError(final boolean isError);
	
	void setResultKind(final ResultKind kind);

	String getScreenShot();

	void setScreenShot(final String screenshot);

	void setContextualTestSentence(final String updatedCommand);

	boolean isFatal();

	ResultKind getResultKind();

	void setMessage(final String parameters);
}