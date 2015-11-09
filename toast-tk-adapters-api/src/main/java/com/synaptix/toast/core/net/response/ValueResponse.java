package com.synaptix.toast.core.net.response;

import com.synaptix.toast.core.net.request.IIdRequest;

public class ValueResponse implements IIdRequest {

	private String id;

	public String value;

	// private BufferedImage screenshot;
	/**
	 * serialization only
	 */
	public ValueResponse() {
	}

	public ValueResponse(
		String id,
		String value) {
		this.id = id;
		this.value = value;
	}

// public ValueResponse(String id, String value, BufferedImage screenshot) {
// this.id = id;
// this.value = value;
// this.screenshot = screenshot;
// }
//
// public BufferedImage getScreenshot() {
// return screenshot;
// }
	@Override
	public String getId() {
		return id;
	}
}
