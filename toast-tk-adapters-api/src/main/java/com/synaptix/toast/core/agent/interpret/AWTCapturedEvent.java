package com.synaptix.toast.core.agent.interpret;

import com.synaptix.toast.core.agent.interpret.IEventInterpreter.EventType;

public class AWTCapturedEvent {

	public String componentLocator = "";

	public String componentName = "";

	public String componentType = "";

	public String businessValue = "";

	public String container = "";

	public String eventLine = "";

	public String eventLabel;

	public long timeStamp;

	private EventType eventType;

	public AWTCapturedEvent() {
	}

	public AWTCapturedEvent(
		String container,
		String locator,
		String name,
		String type,
		String value,
		long timeStamp) {
		this.container = container != null ? container.replace(" ", "_") : "COMMON_CONTAINER";
		this.componentLocator = locator;
		this.componentName = name;
		this.componentType = type;
		this.businessValue = value;
		this.timeStamp = timeStamp;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(
		EventType eventType) {
		this.eventType = eventType;
	}
}
