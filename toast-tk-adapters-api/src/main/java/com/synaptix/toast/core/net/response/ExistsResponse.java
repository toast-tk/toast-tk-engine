package com.synaptix.toast.core.net.response;

public class ExistsResponse {

	public final String id;

	public boolean exists;

	/**
	 * serialization only
	 */
	public ExistsResponse() {
		id = null;
	}

	public ExistsResponse(
		String id,
		boolean b) {
		this.id = id;
		this.exists = b;
	}
}
