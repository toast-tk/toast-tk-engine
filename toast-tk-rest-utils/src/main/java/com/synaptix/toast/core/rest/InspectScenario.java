package com.synaptix.toast.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InspectScenario {

	public String name;

	public String steps = new String();

	protected InspectScenario() {

	}

	public InspectScenario(
		final String item,
		final String recordedSteps
	) {
		this.name = item;
		this.steps = recordedSteps;
	}
}