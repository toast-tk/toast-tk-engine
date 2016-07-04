package io.toast.tk.adapter.web;

import io.toast.tk.dao.domain.api.test.ITestResult;

/**
 * A widget that supports inputs
 * 
 */
public interface HasInputBase<T> {

	/**
	 * sets the input value for the widget 
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
	ITestResult setInput(final T e) throws Exception;
}
