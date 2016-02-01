package com.synaptix.toast.automation.driver.web;

import java.util.List;

import com.synaptix.toast.core.runtime.IWebElementDescriptor;

public interface SynchronizedDriver<E,D> {

	/**
	 * 
	 * @param element
	 *            locator description
	 * @return
	 */
	public E find(
		IWebElementDescriptor element);

	/**
	 * selenium driver
	 * 
	 * @return
	 */
	public D getWebDriver();

	List<E> findAll(IWebElementDescriptor element);
}
