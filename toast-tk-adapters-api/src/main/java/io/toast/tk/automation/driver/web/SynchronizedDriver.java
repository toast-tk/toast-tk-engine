package io.toast.tk.automation.driver.web;

import java.util.List;

import io.toast.tk.core.runtime.IWebElementDescriptor;

public interface SynchronizedDriver<E, D> {

	/**
	 * 
	 * @param element
	 *            locator description
	 * @return
	 */
	E find(final IWebElementDescriptor element);

	/**
	 * selenium driver
	 * 
	 * @return
	 */
	D getWebDriver();

	List<E> findAll(final IWebElementDescriptor element);
}