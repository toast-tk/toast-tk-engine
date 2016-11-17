package io.toast.tk.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InspectScenario {

	public String id;
	
	public String name;

	public String steps = new String();

	protected InspectScenario() {

	}

	public InspectScenario(
			final String id,
		final String item,
		final String recordedSteps
	) {
		this.id = id;
		this.name = item;
		this.steps = recordedSteps;
	}
}