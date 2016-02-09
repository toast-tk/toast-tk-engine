package com.synaptix.toast.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.core.report.TestResult;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class WebPageConfigLine{

	private String elementName;

	private String type;

	private String method;

	private String locator;

	private int position;

	private TestResult result;

	public TestResult getTestResult() {
		return result;
	}

	public WebPageConfigLine(
		String name,
		String type,
		String method,
		String locator,
		Integer position) {
		this.elementName = name;
		this.type = type;
		this.method = method;
		this.locator = locator;
		this.position = position;
	}

	public WebPageConfigLine() {
	}

	/**
	 * @param result
	 */
	public void setResult(
			TestResult result) {
		this.result = result;
	}

	public String getElementName() {
		return elementName;
	}

	public String getType() {
		return type;
	}

	public String getMethod() {
		return method;
	}

	public String getLocator() {
		return locator;
	}

	public Integer getPosition() {
		return position;
	}

	public TestResult getResult() {
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

	public void setMethod(
		String method) {
		this.method = method;
	}

	public void setLocator(
		String locator) {
		this.locator = locator;
	}

	public void setPosition(
		Integer position) {
		this.position = position;
	}
}
