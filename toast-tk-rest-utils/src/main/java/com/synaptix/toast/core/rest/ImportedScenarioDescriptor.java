package com.synaptix.toast.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportedScenarioDescriptor {

	private String type;

	private String rows = new String();

	protected ImportedScenarioDescriptor() {
	}

	public ImportedScenarioDescriptor(
		String type,
		String rows) {
		this.type = type;
		this.rows = rows;
	}

	public String getType() {
		return type;
	}

	public void setType(
		String type) {
		this.type = type;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(
		String rows) {
		this.rows = rows;
	}
}
