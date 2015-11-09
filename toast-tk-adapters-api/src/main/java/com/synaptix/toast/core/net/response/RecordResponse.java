package com.synaptix.toast.core.net.response;

import com.synaptix.toast.core.agent.interpret.AWTCapturedEvent;
import com.synaptix.toast.core.net.request.IIdRequest;

public class RecordResponse implements IIdRequest {

	private String id;

	public AWTCapturedEvent value;

	private String sentence;

	/**
	 * serialization only
	 */
	public RecordResponse() {
	}

	public RecordResponse(
		AWTCapturedEvent eventObject) {
		this.value = eventObject;
	}

	public RecordResponse(
		String sentence) {
		this.sentence = sentence;
	}

	@Override
	public String getId() {
		return id;
	}

	public AWTCapturedEvent getEvent() {
		return value;
	}

	public String getSentence() {
		return sentence;
	}
}
