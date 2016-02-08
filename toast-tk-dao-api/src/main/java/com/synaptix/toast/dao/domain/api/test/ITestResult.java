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
	
	public boolean isFailure();

	public boolean isError();

	void setResultKind(ResultKind kind);

	String getScreenShot();

	void setScreenShot(String screenshot);

	void setContextualTestSentence(String updatedCommand);

	public boolean isFatal();

	ResultKind getResultKind();

	void setMessage(String parameters);


}
