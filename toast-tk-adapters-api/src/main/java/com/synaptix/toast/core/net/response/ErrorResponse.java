package com.synaptix.toast.core.net.response;

import com.synaptix.toast.core.net.request.IIdRequest;

public class ErrorResponse implements IIdRequest {

	private String id;

	private String message;

	public String screenshot;

	/**
	 * serialization only
	 */
	public ErrorResponse() {
	}

	public ErrorResponse(
		String id,
		String message,
		String screenshot) {
		this.id = id;
		this.message = message;
		this.screenshot = screenshot;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String getBase64ScreenShot() {
		return screenshot;
	}

}
