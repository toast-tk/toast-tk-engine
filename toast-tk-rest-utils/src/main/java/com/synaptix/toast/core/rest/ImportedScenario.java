package com.synaptix.toast.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportedScenario {

	private String id;

	private String name;

	protected ImportedScenario() {
	}

	public ImportedScenario(
		String item,
		String name) {
		this.name = item;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(
		String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(
		String name) {
		this.name = name;
	}
}
