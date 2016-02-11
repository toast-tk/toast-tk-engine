package com.synaptix.toast.core.agent.interpret;

public class InterpretedEvent {

	//unused ?
	
	private String eventData;

	private Long timeStamp;

	public InterpretedEvent(final String eventData) {
		this.eventData = eventData;
	}

	public InterpretedEvent(
		final String eventData,
		final Long timeStamp
	) {
		this.eventData = eventData;
		this.timeStamp = timeStamp;
	}

	public String getEventData() {
		return eventData;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}
}