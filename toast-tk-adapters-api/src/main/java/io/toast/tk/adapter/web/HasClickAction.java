package io.toast.tk.adapter.web;

import io.toast.tk.dao.domain.api.test.ITestResult;

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