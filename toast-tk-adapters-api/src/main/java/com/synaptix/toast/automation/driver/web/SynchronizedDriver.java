package com.synaptix.toast.automation.driver.web;

import java.util.List;

import com.synaptix.toast.core.runtime.IWebElement;

public interface SynchronizedDriver<E,D> {

	/**
	 * 
	 * @param element
	 *            locator description
	 * @return
	 */
	public E find(
		IWebElement element);

	/**
	 * selenium driver
	 * 
	 * @return
	 */
	public D getWebDriver();

	List<E> findAll(IWebElement element);
}
