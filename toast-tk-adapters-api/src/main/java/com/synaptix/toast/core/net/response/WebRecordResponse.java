package com.synaptix.toast.core.net.response;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.core.net.request.IIdRequest;

public class WebRecordResponse implements IIdRequest {

	private String id;

	public WebEventRecord value;

	private String sentence;

	public String b64ScreenShot;
	
	/**
	 * serialization only
	 */
	public WebRecordResponse() {
	}

	public WebRecordResponse(
		WebEventRecord eventObject) {
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
