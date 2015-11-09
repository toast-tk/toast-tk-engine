package com.synaptix.toast.core.agent.interpret;

public class InterpretedEvent {

	private String eventData;

	private Long timeStamp;

	public InterpretedEvent(
		String eventData) {
		this.setEventData(eventData);
	}

	public InterpretedEvent(
		String eventData,
		Long timeStamp) {
		this.setEventData(eventData);
		this.setTimeStamp(timeStamp);
	}

	public String getEventData() {
		return eventData;
	}

	private void setEventData(
		String eventData) {
		this.eventData = eventData;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	private void setTimeStamp(
		Long timeStamp) {
		this.timeStamp = timeStamp;
	}
}