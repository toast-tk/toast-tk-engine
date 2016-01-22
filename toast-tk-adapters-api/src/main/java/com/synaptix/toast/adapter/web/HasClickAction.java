package com.synaptix.toast.adapter.web;

import java.util.concurrent.TimeoutException;

import com.synaptix.toast.core.report.TestResult;

/**
 * 
 * @author skokaina
 * 
 */
public interface HasClickAction {

	/**
	 * represent a click action
	 * @throws TimeoutException 
	 * @throws ErrorResultReceivedException 
	 */
	public TestResult click()
		throws Exception;

	/**
	 * represents a double click action
	 */
	public void dbClick();
}
