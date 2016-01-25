package com.synaptix.toast.core.net.request;

public class ScreenShotRequest implements IIdRequest {

	private String id;

	/**
	 * for serialization purpose only
	 */
	public ScreenShotRequest() {
	}

	public ScreenShotRequest(
		String id) {
		this.id = id;
	}


	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getBase64ScreenShot() {
		return null;
	}
}
