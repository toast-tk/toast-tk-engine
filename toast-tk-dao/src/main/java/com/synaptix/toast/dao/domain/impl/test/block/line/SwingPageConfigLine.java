package com.synaptix.toast.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class SwingPageConfigLine {

	private String elementName;

	private String type;

	private String locator;

	private ITestResult result;

	public ITestResult getTestResult() {
		return result;
	}

	public SwingPageConfigLine(
		String name,
		String type,
		String locator) {
		this.elementName = name;
		this.type = type;
		this.locator = locator;
	}

	public SwingPageConfigLine() {
	}

	/**
	 * @param result
	 */
	public void setResult(
			ITestResult result) {
		this.result = result;
	}

	public String getElementName() {
		return elementName;
	}

	public String getType() {
		return type;
	}

	public String getLocator() {
		return locator;
	}

	public ITestResult getResult() {
		return result;
	}

	public void setElementName(
		String elementName) {
		this.elementName = elementName;
	}

	public void setType(
		String type) {
		this.type = type;
	}

	public void setLocator(
		String locator) {
		this.locator = locator;
	}
}
