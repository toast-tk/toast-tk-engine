package com.synaptix.toast.core.net.request;

public class HighLightRequest implements IIdRequest {

	private String locator;

	/**
	 * for serialization purpose only
	 */
	public HighLightRequest() {
	}

	public HighLightRequest(
		String locator) {
		this.setLocator(locator);
	}

	public String getLocator() {
		return locator;
	}

	private void setLocator(
		String locator) {
		this.locator = locator;
	}

	@Override
	public String getId() {
		return null;
	}
}
