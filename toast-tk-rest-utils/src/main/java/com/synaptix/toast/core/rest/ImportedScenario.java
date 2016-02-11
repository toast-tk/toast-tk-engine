package com.synaptix.toast.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportedScenario {

	private String id;

	private String name;

	protected ImportedScenario() {

	}

	public ImportedScenario(
		final String item,
		final String name
	) {
		this.name = item;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}