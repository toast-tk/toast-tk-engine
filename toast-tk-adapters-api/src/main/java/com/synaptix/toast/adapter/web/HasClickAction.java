package com.synaptix.toast.adapter.web;

import java.util.concurrent.TimeoutException;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface HasClickAction {

	/**
	 * represent a click action
	 * @throws TimeoutException 
	 * @throws ErrorResultReceivedException 
	 */
	public ITestResult click()
		throws Exception;

	/**
	 * represents a double click action
	 */
	public void dbClick();
}
