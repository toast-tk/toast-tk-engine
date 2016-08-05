package io.toast.tk.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportedScenarioDescriptor {

	private String type;

	private String rows = new String();

	protected ImportedScenarioDescriptor() {

	}

	public ImportedScenarioDescriptor(
		final String type,
		final String rows
	) {
		this.type = type;
		this.rows = rows;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(final String rows) {
		this.rows = rows;
	}
}