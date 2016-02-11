package com.synaptix.toast.adapter.web;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface HasClickAction {

	/**
	 * represent a click action
	 */
	ITestResult click() throws Exception;

	/**
	 * represents a double click action
	 */
	void dbClick();
}