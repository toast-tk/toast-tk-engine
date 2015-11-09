package com.synaptix.toast.adapter.web;

import java.util.concurrent.TimeoutException;

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
	public boolean click()
		throws Exception;

	/**
	 * represents a double click action
	 */
	public void dbClick();
}
