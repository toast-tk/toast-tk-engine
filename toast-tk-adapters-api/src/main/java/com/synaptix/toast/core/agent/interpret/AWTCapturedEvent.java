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
		final String container,
		final String locator,
		final String name,
		final String type,
		final String value,
		final long timeStamp
	) {
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

	public void setEventType(final EventType eventType) {
		this.eventType = eventType;
	}
}