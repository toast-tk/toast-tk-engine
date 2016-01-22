package com.synaptix.toast.core.net.request;

public class ScanRequest implements IIdRequest {

	private boolean debug;

	private String id;

	public String b64ScreenShot;
	
	/**
	 * for serialization purpose only
	 */
	public ScanRequest() {
	}

	public ScanRequest(
		String id,
		boolean debug) {
		this.setDebug(debug);
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public boolean isDebug() {
		return debug;
	}

	private void setDebug(
		boolean debug) {
		this.debug = debug;
	}

	@Override
	public String getBase64ScreenShot() {
		return b64ScreenShot;
	}
}
