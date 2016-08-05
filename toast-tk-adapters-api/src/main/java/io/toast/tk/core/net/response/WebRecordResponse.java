package io.toast.tk.core.net.response;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.core.net.request.IIdRequest;

public class WebRecordResponse implements IIdRequest {

	private String id;

	public WebEventRecord value;

	public String b64ScreenShot;
	
	/**
	 * serialization only
	 */
	public WebRecordResponse() {
	}

	public WebRecordResponse(final WebEventRecord eventObject) {
		this.value = eventObject;
	}

	@Override
	public String getId() {
		return id;
	}

	public WebEventRecord getEvent() {
		return value;
	}

	@Override
	public String getBase64ScreenShot() {
		return b64ScreenShot;
	}
}